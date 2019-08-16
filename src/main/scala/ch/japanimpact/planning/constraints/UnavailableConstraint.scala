package ch.japanimpact.planning.constraints

import ch.japanimpact.planning._
import jp.kobe_u.copris.{Constraint, Var}

/**
 * Specify that a staff is not available in a given time slot
 *
 * @author Louis Vialar
 */
case class UnavailableConstraint(staff: Staff, slot: Period) extends CustomConstraint {
  override def computeConstraint(variables: Map[Var, StaffAssignation]): Iterable[Constraint] = {
    variables.filter(p => p._2.staff == staff && p._2.slot.slot.isOverlapping(slot))
      .map(_._1 === 0) // These variables must be set to 0 (unavailability slot)
  }
}

object UnavailableConstraint {
  /**
   * Specify that a staff is unavailable during the whole day
   *
   * @param staff the unavailable staff
   * @param day   the day he is not available
   * @return the constraint
   */
  def day(staff: Staff, day: String): UnavailableConstraint = UnavailableConstraint(staff, Period(day, 0, 24 * 60))

  /**
   * Specify that a staff is unavailable one day after a given time
   *
   * @param staff the unavailable staff
   * @param day   the day he is not available
   * @param start the time (in minutes after midnight) since when he is not available
   * @return the constraint
   */
  def after(staff: Staff, day: String, start: Int) = UnavailableConstraint(staff, Period(day, start, 24 * 60))

  /**
   * Specify that a staff is unavailable one day before a given time
   *
   * @param staff the unavailable staff
   * @param day   the day he is not available
   * @param end   the time (in minutes after midnight) since when he is available
   * @return the constraint
   */
  def before(staff: Staff, day: String, end: Int) = UnavailableConstraint(staff, Period(day, 0, end))
}