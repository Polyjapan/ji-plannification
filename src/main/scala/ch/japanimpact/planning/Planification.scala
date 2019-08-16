package ch.japanimpact.planning

import ch.japanimpact.planning.constraints.CustomConstraint
import jp.kobe_u.copris._
import jp.kobe_u.copris.dsl._

/**
 * @author Louis Vialar
 */
class Planification(staffs: List[Staff], tasks: List[TaskSlot], settings: Settings, constraints: List[CustomConstraint] = List()) {

  def planify(allowRecomputation: Boolean = true, recomputation: Int = 0): List[StaffAssignation] = {
    init
    // Create variables
    val assignations = staffs.flatMap(staff => tasks.map(slot => StaffAssignation(slot, staff)))
    val variables = (assignations.map(ass => boolInt(Var(ass.toString))) zip assignations).toMap

    val constraintsApplied = constraints.flatMap(c => c.computeConstraint(variables))
    add(constraintsApplied: _*)

    for (staff <- staffs) {
      // Staffs cannot work more than max
      val sum = Add(tasks.map(slot => Var(StaffAssignation(slot, staff).toString) * slot.slot.duration))
      add(sum < settings.maxTimePerStaff)

      // Staffs cannot work on tasks that are "too difficult" for them
      tasks.filter(_.task.difficulties.nonEmpty)
        .filter(task =>
          // Take only tasks that have
          task.task.difficulties.exists(diff => !staff.abilities.contains(diff))
        )
        .foreach(task => add(task.variable(staff) === 0))
    }

    for (slot <- tasks) {
      // Each task has required number of staffs
      val sum = Add(staffs.map(staff => Var(StaffAssignation(slot, staff).toString)))

      if (!allowRecomputation || recomputation == 0)
        add(sum === slot.staffsRequired)
      else if (recomputation == 1)
        add(And(sum > 0, sum <= slot.staffsRequired))
      else add(sum <= slot.staffsRequired)

      // Each task only has qualified staffs
      staffs.foreach(staff => {
        if (staff.experience < slot.task.minExperience || staff.age < slot.task.minAge)
          add(slot.variable(staff) === 0)
      })

      // Staffs cannot work twice at the same time
      for (slot2 <- tasks if slot2 != slot && slot.slot.day == slot2.slot.day) {
        // Both at the same time
        val s1 = slot.slot
        val s2 = slot2.slot

        if (s1.isOverlapping(s2)) {
          staffs.foreach(staff =>
            add( // no slot | slot 1 | slot 2 | both slots
              Not( // staff doesn't work on both slots (1 | 1 | 1 | 0)
                And( // staff works on both slots (0 | 0 | 0 | 1)
                  slot.variable(staff) === 1, // staff works on slot 1
                  slot2.variable(staff) === 1) // staff works on slot 2
              )))
        }
      }
    }

    println("Constraints (computation " + recomputation + ")")
    show
    println("Solving " + recomputation)

    if (recomputation > 0) {
      println()
      println("!!! ATTENTION !!!")
      println("This is a recomputation! Some slots may be understaffed/not staffed at all!")
      println("This is randomly done, if you want to avoid this, remove some other slots")
      println()
    }

    if (find) solution.intValues.filter(p => p._2 > 0).map(p => variables(p._1)).toList
    else if (allowRecomputation && recomputation < 2) planify(allowRecomputation, recomputation + 1)
    else List.empty[StaffAssignation]
  }
}
