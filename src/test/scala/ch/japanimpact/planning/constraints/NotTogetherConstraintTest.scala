package ch.japanimpact.planning.constraints

import ch.japanimpact.planning.{Planification, Settings, Staff, Task, TaskSlot, Period}
import org.scalatest._

/**
 * @author Louis Vialar
 */
class NotTogetherConstraintTest extends FlatSpec {
  val (s1, s2) = (Staff(1, "Staff A", 18, 10), Staff(2, "Staff B", 18, 10))

  "Not together constraint" should "not put two persons in same task" in {
    assert(new Planification(
      List(s1, s2),
      List(
        TaskSlot(Task("Task A", 0, 0), Period("samedi", 10 * 60, 11 * 60), 2)
      ),
      Settings(1000),
      List(NotTogetherConstraint(s1, s2))
    ).planify(false).isEmpty)
  }

  it should "not avoid assignation to different tasks" in {
    assert(new Planification(
      List(s1, s2),
      List(
        TaskSlot(Task("Task A", 0, 0), Period("samedi", 10 * 60, 11 * 60), 1),
        TaskSlot(Task("Task B", 0, 0), Period("samedi", 10 * 60 + 30, 11 * 60), 1)
      ),
      Settings(1000),
      List(NotTogetherConstraint(s1, s2))
    ).planify().size == 2)
  }
}
