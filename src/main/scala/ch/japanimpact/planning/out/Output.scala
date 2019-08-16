package ch.japanimpact.planning.out

import java.io.{File, IOException}

import ch.japanimpact.planning.StaffAssignation

/**
 * @author Louis Vialar
 */
trait Output {
  @throws[IOException]
  def writeOutput(baseDir: File, content: List[StaffAssignation]): Unit
}
