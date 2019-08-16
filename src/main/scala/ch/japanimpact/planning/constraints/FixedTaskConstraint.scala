package ch.japanimpact.planning.constraints

import ch.japanimpact.planning._
import jp.kobe_u.copris.{Constraint, Var}

/**
 * @author Louis Vialar
 */
case class FixedTaskConstraint(staff: Staff, task: Task) extends CustomConstraint {
  override def computeConstraint(variables: Map[Var, StaffAssignation]): Iterable[Constraint] = {
    // Block other tasks
    variables.filter(_._2.slot.task != task).map(_._1 === 0)
  }
}
