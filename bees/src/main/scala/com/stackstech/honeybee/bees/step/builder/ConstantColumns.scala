
package com.stackstech.honeybee.bees.step.builder

/**
 * for dsl rules, the constant columns might be used during calculation,
 */
object ConstantColumns {
  val tmst = "__tmst"
  val metric = "__metric"
  val record = "__record"
  val empty = "__empty"

  val beginTs = "__begin_ts"
  val endTs = "__end_ts"

  val distinct = "__distinct"

  val rowNumber = "__rn"

  val columns: List[String] =
    List[String](tmst, metric, record, empty, beginTs, endTs, distinct, rowNumber)
}
