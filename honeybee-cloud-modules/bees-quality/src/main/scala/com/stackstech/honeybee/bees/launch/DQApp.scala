
package com.stackstech.honeybee.bees.launch

import scala.util.Try

import org.apache.spark.metrics.sink.Sink
import org.apache.spark.sql.SparkSession

import com.stackstech.honeybee.bees.Loggable
import com.stackstech.honeybee.bees.configuration.dqdefinition.{DQConfig, EnvConfig, SinkParam}

/**
 * dq application process
 */
trait DQApp extends Loggable with Serializable {

  val envParam: EnvConfig
  val dqParam: DQConfig

  implicit var sparkSession: SparkSession = _

  def init: Try[_]

  /**
   * @return execution success
   */
  def run: Try[Boolean]

  def close: Try[_]

  /**
   * application will exit if it fails in run phase.
   * if retryable is true, the exception will be threw to spark env,
   * and enable retry strategy of spark application
   */
  def retryable: Boolean

  /**
   * timestamp as a key for metrics
   */
  protected def getMeasureTime: Long = {
    dqParam.getTimestampOpt match {
      case Some(t) if t > 0 => t
      case _ => System.currentTimeMillis
    }
  }

  /**
   * Gets a valid [[Sink]] definition from the Env Config for each [[Sink]] defined in Job Config.
   *
   * @throws AssertionError if Env Config does not contain definition for a sink defined in Job Config
   * @return [[Seq]] of [[Sink]] definitions
   */
  protected def getSinkParams: Seq[SinkParam] = {
    val sinkParams = dqParam.getSinkNames
      .map(_.toLowerCase())
      .map { sinkName =>
        (sinkName, envParam.getSinkParams.find(_.getName.toLowerCase().matches(sinkName)))
      }

    val missingSinks = sinkParams.filter(_._2.isEmpty).map(_._1)

    assert(
      missingSinks.isEmpty,
      s"Sink(s) ['${missingSinks.mkString("', '")}'] not defined in env config.")

    sinkParams.flatMap(_._2)
  }

}
