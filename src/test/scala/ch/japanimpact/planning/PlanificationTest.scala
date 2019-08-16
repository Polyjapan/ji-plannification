package ch.japanimpact.planning

/**
 * @author Louis Vialar
 */
class PlanificationTest extends org.scalatest.FlatSpec {
  "Planification" should "not allow overlap" in {
    assert(new Planification(
      List(Staff(1, "Staff", 18, 10)),
      List(
        TaskSlot(Task("Task A", 0, 0), Period("samedi", 10 * 60, 11 * 60), 1),
        TaskSlot(Task("Task B", 0, 0), Period("samedi", 10 * 60 + 30, 11 * 60), 1)
      ),
      Settings(1000), Nil
    ).planify(false).isEmpty)
  }

  it should "allow basic assignation" in {
    assert(new Planification(
      List(Staff(1, "Staff", 18, 10)),
      List(
        TaskSlot(Task("Task C", 0, 0), Period("samedi", 10 * 60, 11 * 60), 1)
      ),
      Settings(1000), Nil
    ).planify().nonEmpty)
    assert(new Planification(
      List(
        Staff(1, "Staff", 18, 10),
        Staff(2, "Staff B", 18, 10)
      ),
      List(
        TaskSlot(Task("Task C", 0, 0), Period("samedi", 10 * 60, 11 * 60), 1),
        TaskSlot(Task("Task X", 0, 0), Period("samedi", 10 * 60, 11 * 60), 1)
      ),
      Settings(1000), Nil
    ).planify().nonEmpty)
  }

  it should "not allow max time overrun" in {
    assert(new Planification(
      List(Staff(1, "Staff", 18, 10)),
      List(
        TaskSlot(Task("Task D", 0, 0), Period("samedi", 10 * 60, 15 * 60), 1)
      ),
      Settings(60), Nil
    ).planify().isEmpty)
  }

  it should "not allow understaffed tasks" in {
    assert(new Planification(
      List(Staff(1, "Staff", 18, 10)),
      List(
        TaskSlot(Task("Task E", 0, 0), Period("samedi", 10 * 60, 15 * 60), 2)
      ),
      Settings(1000), Nil
    ).planify(allowRecomputation = false).isEmpty)
  }

  it should "not overstaff tasks" in {
    assert(new Planification(
      List(
        Staff(1, "Staff", 18, 10),
        Staff(2, "Staff 2", 18, 10)
      ),
      List(
        TaskSlot(Task("Task F", 0, 0), Period("samedi", 10 * 60, 15 * 60), 1)
      ),
      Settings(1000), Nil
    ).planify().size == 1)
  }

  it should "not give too difficult tasks" in {
    assert(new Planification(
      List(Staff(1, "Staff", 18, 10, Physical)),
      List(
        TaskSlot(Task("Task G", 0, 0), Period("samedi", 10 * 60, 15 * 60), 1)
      ),
      Settings(1000), Nil
    ).planify().nonEmpty)

    assert(new Planification(
      List(Staff(1, "Staff", 18, 10)),
      List(
        TaskSlot(Task("Task H", 0, 0, Physical), Period("samedi", 10 * 60, 15 * 60), 1)
      ),
      Settings(1000), Nil
    ).planify().isEmpty)
  }

  it should "not give over-age tasks" in {
    assert(new Planification(
      List(Staff(1, "Staff", 18, 10)),
      List(
        TaskSlot(Task("Task I", 20, 0), Period("samedi", 10 * 60, 15 * 60), 1)
      ),
      Settings(1000), Nil
    ).planify().isEmpty)
  }

  it should "not give over-skilled tasks" in {
    assert(new Planification(
      List(Staff(1, "Staff", 18, 0)),
      List(
        TaskSlot(Task("Task J", 0, 10), Period("samedi", 10 * 60, 15 * 60), 1)
      ),
      Settings(1000), Nil
    ).planify().isEmpty)
  }

}
