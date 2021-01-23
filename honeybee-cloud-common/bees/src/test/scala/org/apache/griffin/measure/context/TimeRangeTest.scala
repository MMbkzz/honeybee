
package com.stackstech.honeybee.bees.context

import org.scalatest._

class TimeRangeTest extends FlatSpec with Matchers {

  "time range" should "be able to merge another time range" in {
    val tr1 = TimeRange(1, 10, Set(2, 5, 8))
    val tr2 = TimeRange(4, 15, Set(5, 6, 13, 7))
    tr1.merge(tr2) should be(TimeRange(1, 15, Set(2, 5, 6, 7, 8, 13)))
  }

  it should "get minimum timestamp in not-empty timestamp set" in {
    val tr = TimeRange(1, 10, Set(2, 5, 8))
    tr.minTmstOpt should be(Some(2))
  }

  it should "not get minimum timestamp in empty timestamp set" in {
    val tr = TimeRange(1, 10, Set[Long]())
    tr.minTmstOpt should be(None)
  }

}
