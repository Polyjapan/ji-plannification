package ch.japanimpact.planning
import ch.japanimpact.planning.constraints.CustomConstraint
import jp.kobe_u.copris._
import jp.kobe_u.copris.dsl._
/**
 * @author Louis Vialar
 */
object Main {
  // Basic tests for now

  def main(args: Array[String]): Unit = {
    // Tests

    val staffs = List(
      Staff(1, "Aymeric", 20, 5),
      Staff(2, "Kevina", 15, 0),
      Staff(3, "Alberto", 18, 0),
      Staff(4, "Guestre", 18, 3)
    )

    val tasks = List(
      TaskSlot(Task("Vider poubelles", 0, 0), Period("samedi", 8 * 60, 9 * 60), 2),
      TaskSlot(Task("Vider poubelles", 0, 0), Period("samedi", 14 * 60, 15 * 60), 2),
      TaskSlot(Task("Vider poubelles", 0, 0), Period("dimanche", 12 * 60, 13 * 60), 2),
      TaskSlot(Task("Tenir caisses", 18, 0), Period("samedi", 8 * 60, 18 * 60), 1),
      TaskSlot(Task("Gérer caisses", 18, 4), Period("samedi", 8 * 60, 18 * 60), 1)
    )

    val constraints: List[CustomConstraint] = List()

    val plan = new Planification(staffs, tasks, Settings(50 * 60), constraints)

    plan.planify().map(_.prettyString).foreach(println)
  }
}
