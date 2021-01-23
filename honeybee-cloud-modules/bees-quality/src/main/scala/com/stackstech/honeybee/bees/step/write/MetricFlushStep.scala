
package com.stackstech.honeybee.bees.step.write

import scala.util.Try

import com.stackstech.honeybee.bees.context.DQContext

/**
 * flush final metric map in context and write
 */
case class MetricFlushStep() extends WriteStep {

  val name: String = ""
  val inputName: String = ""
  val writeTimestampOpt: Option[Long] = None

  def execute(context: DQContext): Try[Boolean] = Try {
    context.metricWrapper.flush.foldLeft(true) { (ret, pair) =>
      val (t, metric) = pair
      val pr = try {
        context.getSinks(t).foreach { sink =>
          try {
            sink.sinkMetrics(metric)
          } catch {
            case e: Throwable => error(s"sink metrics error: ${e.getMessage}", e)
          }
        }
        true
      } catch {
        case e: Throwable =>
          error(s"flush metrics error: ${e.getMessage}", e)
          false
      }
      ret && pr
    }
  }

}
