package ch.japanimpact.planning.constraints

import ch.japanimpact.planning
import ch.japanimpact.planning.Staff
import jp.kobe_u.copris.{And, Constraint, Not, Var}

/**
 * @author Louis Vialar
 */
case class NotTogetherConstraint(staff1: Staff, staff2: Staff) extends CustomConstraint {
  override def computeConstraint(variables: Map[Var, planning.StaffAssignation]): Iterable[Constraint] = {
    variables
      .groupBy(_._2.slot)
      .filter(_._1.staffsRequired >= 2) // lone tasks we don't care
      .flatMap(pair => {
        // Values are maps [var <> staff assignation] for the task
        val vars = pair._2.filter(p => {
          val staff = p._2.staff
          staff == staff1 || staff == staff2
        }).keys.toList // Keep only vars

        // Here build conditions -- we should only have two vars
        if (vars.size < 2) None
        else Some(
          Not( // not both work on same task
            And( // both work on same task
              vars.head === 1, // staff 1 works
              vars(1) === 1) // staff 2 works
          ))
      })
  }
}
