
package com.stackstech.honeybee.bees.context

import scala.math.{max, min}

case class TimeRange(begin: Long, end: Long, tmsts: Set[Long]) extends Serializable {
  def merge(tr: TimeRange): TimeRange = {
    TimeRange(min(begin, tr.begin), max(end, tr.end), tmsts ++ tr.tmsts)
  }
  def minTmstOpt: Option[Long] = {
    try {
      if (tmsts.nonEmpty) Some(tmsts.min) else None
    } catch {
      case _: Throwable => None
    }
  }
}

object TimeRange {
  val emptyTimeRange: TimeRange = TimeRange(0, 0, Set[Long]())
  def apply(range: (Long, Long), tmsts: Set[Long]): TimeRange =
    TimeRange(range._1, range._2, tmsts)
  def apply(ts: Long, tmsts: Set[Long]): TimeRange = TimeRange(ts, ts, tmsts)
  def apply(ts: Long): TimeRange = TimeRange(ts, ts, Set[Long](ts))
  def apply(tmsts: Set[Long]): TimeRange = {
    try {
      TimeRange(tmsts.min, tmsts.max, tmsts)
    } catch {
      case _: Throwable => emptyTimeRange
    }
  }
}
