package ch.japanimpact.planning.constraints

import ch.japanimpact.planning
import ch.japanimpact.planning.Staff
import jp.kobe_u.copris.{Add, And, Constraint, Var}

/**
 * This constraint always put staffs together on paired jobs
 *
 * @author Louis Vialar
 */
case class TogetherConstraint(staff1: Staff, staff2: Staff) extends CustomConstraint {
  override def computeConstraint(variables: Map[Var, planning.StaffAssignation]): Iterable[Constraint] = {
    variables
      .groupBy(_._2.slot)
      .filter(_._1.staffsRequired >= 2) // lone tasks we don't care
      .flatMap(pair => {
        val vars = pair._2.filter(p => {
          val staff = p._2.staff
          staff == staff1 || staff == staff2
        }).keys.toList // Keep only vars

        // Here build conditions -- we should only have two vars
        if (vars.size < 2) Nil
        else Some(And(vars.head === 1, vars(1) === 1))
      })
  }
}
