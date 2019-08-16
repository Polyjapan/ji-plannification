package ch.japanimpact.planning.out

import java.io.{File, IOException}

import ch.japanimpact.planning._

/**
 * @author Louis Vialar
 */
trait SplitOutput extends Output {
  @throws[IOException]
  def writeByStaff(basePath: File, staffs: Map[Staff, List[StaffAssignation]]): Unit

  @throws[IOException]
  def writeByTask(basePath: File, tasks: Map[Task, List[StaffAssignation]]): Unit

  @throws[IOException]
  override def writeOutput(baseDir: File, content: List[StaffAssignation]): Unit = {
    writeByStaff(baseDir,
      content.groupBy(a => a.staff)
        .mapValues(list => list.sortBy(_.slot.slot.timeStart)))

    writeByTask(baseDir,
      content.groupBy(a => a.slot.task)
        .mapValues(list => list.sortBy(_.slot.slot.timeStart)))
  }
}
