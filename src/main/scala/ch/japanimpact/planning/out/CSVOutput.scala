package ch.japanimpact.planning.out
import java.io.{File, PrintStream}

import ch.japanimpact.planning
import ch.japanimpact.planning._

/**
 * @author Louis Vialar
 */
object CSVOutput extends SplitOutput {
  override def writeByStaff(basePath: File, staffs: Map[planning.Staff, List[planning.StaffAssignation]]): Unit = {
    val staffPath = new File(basePath, "staffs")

    if (!staffPath.exists()) staffPath.mkdir()

    staffs.foreach(pair => {
      val file = new File(staffPath, "staff_" + pair._1.id + ".csv")
      file.createNewFile()

      val out = new PrintStream(file)

      out.println("Task name,Day,Start,End,Start minutes,End minutes")

      pair._2.map {
        case StaffAssignation(TaskSlot(Task(name, _, _, _), slot, _), _) =>
          name + "," + slot.day + "," + slot.prettyStart + "," + slot.prettyEnd + "," + slot.timeStart + "," + slot.timeEnd
      } foreach (out.println)

      out.close()
    })
  }

  override def writeByTask(basePath: File, tasks: Map[planning.Task, List[planning.StaffAssignation]]): Unit = {
    val taskPath = new File(basePath, "tasks")

    if (!taskPath.exists()) taskPath.mkdir()

    tasks.foreach(pair => {
      val file = new File(taskPath, "task_" + pair._1.name.replaceAll(" ", "_") + ".csv")
      file.createNewFile()

      val out = new PrintStream(file)
      out.println("Staff Id,Staff Name,Day,Start,End,Start minutes,End minutes")

      pair._2.map {
        case StaffAssignation(TaskSlot(_, slot, _), Staff(id, name,  _, _, _)) =>
          id +"," + name + "," + slot.day + "," + slot.prettyStart + "," + slot.prettyEnd + "," + slot.timeStart + "," + slot.timeEnd
      } foreach (out.println)

      out.close()
    })
  }
}
