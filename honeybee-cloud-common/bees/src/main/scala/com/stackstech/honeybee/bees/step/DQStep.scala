
package com.stackstech.honeybee.bees.step

import scala.util.Try

import com.stackstech.honeybee.bees.Loggable
import com.stackstech.honeybee.bees.context.DQContext

trait DQStep extends Loggable {

  val name: String

  /**
   * @return execution success
   */
  def execute(context: DQContext): Try[Boolean]

  def getNames: Seq[String] = name :: Nil

}

object DQStepStatus extends Enumeration {
  val PENDING: DQStepStatus.Value = Value
  val RUNNING: DQStepStatus.Value = Value
  val COMPLETE: DQStepStatus.Value = Value
  val FAILED: DQStepStatus.Value = Value
}
