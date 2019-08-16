package ch.japanimpact.planning.constraints

import ch.japanimpact.planning._
import org.scalatest._

/**
 * @author Louis Vialar
 */
class TogetherConstraintTest extends FlatSpec {
  val (s1, s2, s3) =
    (Staff(1, "Staff A", 18, 10), Staff(2, "Staff B", 18, 10), Staff(3, "Staff C", 18, 10))

  val (t1, t2) = (
    TaskSlot(Task("Task A", 0, 0), Period("samedi", 10 * 60, 11 * 60), 2),
    TaskSlot(Task("Task B", 0, 0), Period("samedi", 10 * 60, 11 * 60), 1))

  "Together constraint" should "try to put two persons in same task" in {
    val result = new Planification(
      List(s1, s2, s3),
      List(t1, t2),
      Settings(1000),
      List(TogetherConstraint(s1, s2))
    ).planify()

    assert(result.nonEmpty)
    if (result.contains(t1.assign(s1)))
      assert(result.contains(t1.assign(s2)))
    else if (result.contains(t1.assign(s2)))
      assert(result.contains(t1.assign(s1)))
    else fail(result.toString())
  }
}
