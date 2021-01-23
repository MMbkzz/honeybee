
package com.stackstech.honeybee.bees.step.read

import org.apache.spark.sql._

import com.stackstech.honeybee.bees.context.DQContext
import com.stackstech.honeybee.bees.utils.DataFrameUtil._

case class UnionReadStep(name: String, readSteps: Seq[ReadStep]) extends ReadStep {

  val config: Map[String, Any] = Map()
  val cache: Boolean = false

  def read(context: DQContext): Option[DataFrame] = {
    val dfOpts = readSteps.map { readStep =>
      readStep.read(context)
    }
    if (dfOpts.nonEmpty) {
      dfOpts.reduce((a, b) => unionDfOpts(a, b))
    } else None
  }

}
