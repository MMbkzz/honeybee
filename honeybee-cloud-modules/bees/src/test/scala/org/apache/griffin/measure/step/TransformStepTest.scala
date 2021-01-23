
package com.stackstech.honeybee.bees.step

import org.scalatest._
import scala.util.Try

import com.stackstech.honeybee.bees.{Loggable, SparkSuiteBase}
import com.stackstech.honeybee.bees.configuration.enums.ProcessType.BatchProcessType
import com.stackstech.honeybee.bees.context.{ContextId, DQContext}
import com.stackstech.honeybee.bees.step.transform.TransformStep

class TransformStepTest extends FlatSpec with Matchers with SparkSuiteBase with Loggable {

  case class DualTransformStep(
      name: String,
      duration: Int,
      rule: String = "",
      details: Map[String, Any] = Map(),
      cache: Boolean = false)
      extends TransformStep {

    def doExecute(context: DQContext): Try[Boolean] = Try {
      val threadName = Thread.currentThread().getName
      info(s"Step $name started with $threadName")
      Thread.sleep(duration * 1000L)
      info(s"Step $name finished with $threadName")
      true
    }
  }

  private def getDqContext(name: String = "test-context"): DQContext = {
    DQContext(ContextId(System.currentTimeMillis), name, Nil, Nil, BatchProcessType)(spark)
  }

  /**
   * Run transform steps in parallel. Here are the dependencies of transform steps
   *
   * step5
   * |   |---step2
   * |   |   |---step1
   * |   |---step3
   * |   |   |---step1
   * |   |---step4
   *
   * step1 : -->
   * step2 :    --->
   * step3 :    ---->
   * step4 : ->
   * step5 :         -->
   *
   */
  "transform step " should "be run steps in parallel" in {
    val step1 = DualTransformStep("step1", 3)
    val step2 = DualTransformStep("step2", 4)
    step2.parentSteps += step1
    val step3 = DualTransformStep("step3", 5)
    step3.parentSteps += step1
    val step4 = DualTransformStep("step4", 2)
    val step5 = DualTransformStep("step5", 3)
    step5.parentSteps += step2
    step5.parentSteps += step3
    step5.parentSteps += step4

    val context = getDqContext()
    step5.execute(context).get should be(true)
  }
}
