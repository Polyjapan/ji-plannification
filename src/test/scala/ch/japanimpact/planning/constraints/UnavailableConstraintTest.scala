package ch.japanimpact.planning.constraints

import ch.japanimpact.planning.{Planification, Settings, Staff, Task, TaskSlot, Period}
import org.scalatest._

/**
 * @author Louis Vialar
 */
class UnavailableConstraintTest extends FlatSpec {

  val s1 = Staff(1, "Staff A", 18, 10)

  val t1 = TaskSlot(Task("Task A", 0, 0), Period("samedi", 10 * 60, 15 * 60), 1)


  "Unavailable Constraint" should "not use an unavailable staff" in {
    assert(new Planification(List(s1), List(t1),
      Settings(1000),
      List(UnavailableConstraint(s1, Period("samedi", 11 * 60, 12 * 60)))
    ).planify().isEmpty)

    assert(new Planification(List(s1), List(t1),
      Settings(1000),
      List(UnavailableConstraint(s1, Period("samedi", 9 * 60, 16 * 60)))
    ).planify().isEmpty)

    assert(new Planification(List(s1), List(t1),
      Settings(1000),
      List(UnavailableConstraint(s1, Period("samedi", 8 * 60, 9 * 60)))
    ).planify().nonEmpty)
  }

  "Unavailable Day Constraint" should "not use an unavailable staff" in {
    assert(new Planification(List(s1), List(t1),
      Settings(1000),
      List(UnavailableConstraint.day(s1, "samedi"))
    ).planify().isEmpty)

    assert(new Planification(List(s1), List(t1),
      Settings(1000),
      List(UnavailableConstraint.day(s1, "dimanche"))
    ).planify().nonEmpty)
  }

  "Unavailable Before Constraint" should "not use an unavailable staff" in {
    assert(new Planification(List(s1), List(t1),
      Settings(1000),
      List(UnavailableConstraint.before(s1, "samedi", 12 * 60))
    ).planify().isEmpty)

    assert(new Planification(List(s1), List(t1),
      Settings(1000),
      List(UnavailableConstraint.before(s1, "samedi", 16 * 60))
    ).planify().isEmpty)

    assert(new Planification(List(s1), List(t1),
      Settings(1000),
      List(UnavailableConstraint.before(s1, "samedi", 4 * 60))
    ).planify().nonEmpty)
  }

  "Unavailable After Constraint" should "not use an unavailable staff" in {
    assert(new Planification(List(s1), List(t1),
      Settings(1000),
      List(UnavailableConstraint.after(s1, "samedi", 12 * 60))
    ).planify().isEmpty)

    assert(new Planification(List(s1), List(t1),
      Settings(1000),
      List(UnavailableConstraint.after(s1, "samedi", 6 * 60))
    ).planify().isEmpty)

    assert(new Planification(List(s1), List(t1),
      Settings(1000),
      List(UnavailableConstraint.after(s1, "samedi", 18 * 60))
    ).planify().nonEmpty)
  }
}
