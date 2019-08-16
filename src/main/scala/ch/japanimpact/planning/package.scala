package ch.japanimpact

import jp.kobe_u.copris.Var

/**
 * @author Louis Vialar
 */
package object planning {

  case class Staff(id: Int, name: String, age: Int, experience: Int, abilities: Difficulty*) {
    override def toString: String = "staff-" + id
  }

  case class Period(day: String, timeStart: Int, timeEnd: Int) {
    override def toString: String = day + "-" + timeStart + "-" + timeEnd

    private def prettyTime(time: Int): String = {
      val hrs = time / 60
      val min = time % 60

      s"$hrs:$min"
    }

    def prettyStart: String = prettyTime(timeStart)

    def prettyEnd: String = prettyTime(timeEnd)

    def duration: Int = timeEnd - timeStart

    def isOverlapping(other: Period): Boolean = {
      val s1 = this
      val s2 = other

      day == other.day &&
        ((s1.timeStart <= s2.timeStart && s1.timeEnd >= s2.timeStart) || // s1 starts before s2 but finishes after s2 starts
        (s2.timeStart <= s1.timeStart && s2.timeEnd >= s1.timeStart) || // s2 starts before s1 but finishes after s1 starts
        (s1.timeStart >= s2.timeStart && s1.timeEnd <= s2.timeEnd) || // s1 starts after s2 and finishes before s2
        (s2.timeStart >= s1.timeStart && s2.timeEnd <= s1.timeEnd)) // s2 starts after s1 and finishes befire s1
    }

  }

  case class Task(name: String, minAge: Int, minExperience: Int, difficulties: Difficulty*) {
    override def toString: String = name
  }

  case class TaskSlot(task: Task, slot: Period, staffsRequired: Int) {
    override def toString: String = task.toString + "-at-" + slot.toString

    def assign(staff: Staff) = StaffAssignation(this, staff)

    def variable(staff: Staff): Var = assign(staff).variable
  }

  case class Settings(maxTimePerStaff: Int)

  case class StaffAssignation(slot: TaskSlot, staff: Staff) {
    override def toString: String = slot.toString + "-by-" + staff.toString

    def prettyString: String = "Tache " + slot.task.name + " le " + slot.slot.day + " de " + slot.slot.prettyStart + " à " + slot.slot.prettyEnd + " : " + staff.name + " (#" + staff.id + ")"

    def variable: Var = Var(toString)
  }

  sealed trait Difficulty

  /**
   * Physical difficulty (moving huge stuff)
   */
  case object Physical extends Difficulty

  /**
   * Contact with clients
   */
  case object ClientContact extends Difficulty

  /**
   * Contact with naked persons (changing rooms), used to avoid putting staffs that are known to have caused problems
   * with that previously
   */
  case object NakedContact extends Difficulty

  case class OtherDifficulty(difficulty: String) extends Difficulty

}
