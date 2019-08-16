package ch.japanimpact.planning.in

import ch.japanimpact.planning._
import ch.japanimpact.planning.in.TimePartProperties._

/**
 * @author Louis Vialar
 */
case class TaskDescriptor(task: Task, parts: TimePart*) {
  def produceSlots: Iterable[TaskSlot] = parts.flatMap(_.produceSlots(task))
}

/**
 * Describes a part of the time of a task
 *
 * @param slot           the period of the task
 * @param requiredStaffs the number of staffs required during this time
 * @param splitIn        how many shifts should be produced for this part of the timing
 */
case class TimePart(slot: Period, requiredStaffs: Int, splitIn: Int, settings: TimePartProperty*) {
  def produceSlots(task: Task): Iterable[TaskSlot] = {
    val totalDuration = slot.timeEnd - slot.timeStart
    val shiftDuration = totalDuration / splitIn

    if (!settings.contains(PartialShiftChange)) {
      0 until splitIn map (i => {

        val start = slot.timeStart + i * shiftDuration
        val end = start + shiftDuration

        TaskSlot(task, Period(slot.day, start, end), requiredStaffs)
      })
    } else {
      // TODO: check no incompatible settings
      0 until splitIn flatMap (i => {
        val shiftedNum = requiredStaffs / 2
        val normalNum = requiredStaffs - shiftedNum
        val shift = shiftDuration / 2

        val start = slot.timeStart + i * shiftDuration
        val end = start + shiftDuration

        val (shiftStart, shiftEnd) = {
          if (i == 0) {
            val s = if (settings.contains(FirstPartialShiftStartsLater)) start + shift else start
            val e = if (settings.contains(FirstPartialShiftEndsEarlier)) end - shift else end + shift
            (s, e)
          } else if (i == splitIn - 1) {
            val s = if (settings.contains(FirstPartialShiftEndsEarlier)) start - shift else start + shift
            val e =
              if (settings.contains(LastPartialShiftEndsEarlier))
                start + shift
              else if (settings.contains(LastPartialShiftEndsLater))
                end + shift
              else
                end

            (s, e)
          } else {
            val s = if (settings.contains(FirstPartialShiftEndsEarlier)) start - shift else start + shift
            (s, s + shiftDuration)
          }
        }

        if (shiftStart < shiftEnd) {
          List(
            TaskSlot(task, Period(slot.day, start, end), normalNum),
            TaskSlot(task, Period(slot.day, shiftStart, shiftEnd), shiftedNum)
          )
        } else {
          // Case where shifted shift is 0 in length
          List(TaskSlot(task, Period(slot.day, start, end), normalNum))
        }
      })
    }


  }
}

object TimePartProperties {

  sealed trait TimePartProperty

  case object PartialShiftChange extends TimePartProperty

  case object LastPartialShiftEndsLater extends TimePartProperty

  case object LastPartialShiftEndsEarlier extends TimePartProperty

  case object LastPartialShiftEndsSameTime extends TimePartProperty

  case object FirstPartialShiftStartsLater extends TimePartProperty

  case object FirstPartialShiftEndsEarlier extends TimePartProperty

  case object FirstPartialShiftEndsLater extends TimePartProperty

}
