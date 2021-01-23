
package com.stackstech.honeybee.bees.step

import scala.util.{Failure, Success, Try}

import com.stackstech.honeybee.bees.context.DQContext

/**
 * sequence of dq steps
 */
case class SeqDQStep(dqSteps: Seq[DQStep]) extends DQStep {

  val name: String = ""
  val rule: String = ""
  val details: Map[String, Any] = Map()

  /**
   * @return execution success
   */
  def execute(context: DQContext): Try[Boolean] = {
    dqSteps
      .map(_.execute(context))
      .foldLeft(Try(true))((ret, stepResult) => {
        (ret, stepResult) match {
          case (Success(_), nextResult) => nextResult
          case (Failure(_), _) => ret
        }
      })
  }

  override def getNames: Seq[String] = {
    dqSteps.foldLeft(Nil: Seq[String]) { (ret, dqStep) =>
      ret ++ dqStep.getNames
    }
  }

}
