package ch.japanimpact.planning.constraints

import ch.japanimpact.planning._
import jp.kobe_u.copris.{Constraint, Var}

/**
 * @author Louis Vialar
 */
case class FixedTaskSlotConstraint(staff: Staff, task: TaskSlot) extends CustomConstraint {
  override def computeConstraint(variables: Map[Var, StaffAssignation]): Iterable[Constraint] = {
    Some(task.variable(staff) === 1)
  }
}
