package ch.japanimpact.planning.constraints

import ch.japanimpact.planning.StaffAssignation
import jp.kobe_u.copris.{Constraint, Var}

/**
 * @author Louis Vialar
 */
trait CustomConstraint {
  import jp.kobe_u.copris.dsl._

  def computeConstraint(variables: Map[Var, StaffAssignation]): Iterable[Constraint]
}
