
package com.stackstech.honeybee.bees.job

import scala.util.{Failure, Success, Try}

import com.stackstech.honeybee.bees.context.DQContext
import com.stackstech.honeybee.bees.step.DQStep

case class DQJob(dqSteps: Seq[DQStep]) extends Serializable {

  def execute(context: DQContext): Try[Boolean] = {
    dqSteps
      .map(_.execute(context))
      .foldLeft(Try(true)) { (ret, stepResult) =>
        (ret, stepResult) match {
          case (Success(_), nextResult) => nextResult
          case (Failure(_), _) => ret
        }
      }
  }

}
