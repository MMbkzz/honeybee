
package com.stackstech.honeybee.bees.sink

import scala.util.{Failure, Success, Try}

import com.stackstech.honeybee.bees.Loggable
import com.stackstech.honeybee.bees.configuration.dqdefinition.SinkParam
import com.stackstech.honeybee.bees.configuration.enums.SinkType._
import com.stackstech.honeybee.bees.utils.ParamUtil._

/**
 * SinkFactory, responsible for creation of Batch and Streaming Sinks based on the definition
 * provided in Env Config.
 *
 * @param sinkParamIter [[Seq]] of sink definitions as [[SinkParam]]
 * @param jobName name of the current Griffin Job
 */
case class SinkFactory(sinkParamIter: Seq[SinkParam], jobName: String)
    extends Loggable
    with Serializable {

  /**
   * Creates all the sinks defined in the Env Config.
   *
   * @param timeStamp epoch timestamp
   * @param block persist in blocking or non-blocking way
   * @return a [[Seq]] of [[Sink]] that were created successfully
   */
  def getSinks(timeStamp: Long, block: Boolean): Seq[Sink] = {
    sinkParamIter.flatMap(param => getSink(timeStamp, param, block))
  }

  /**
   * Creates a [[Sink]] from the definition provided in the Env Config.
   * Supported [[Sink]] are defined in [[SinkType]].
   *
   * @param timeStamp epoch timestamp
   * @param sinkParam sink definition
   * @param block persist in blocking or non-blocking way
   * @return [[Some]](sink) if successfully created sink else [[None]]
   */
  private def getSink(timeStamp: Long, sinkParam: SinkParam, block: Boolean): Option[Sink] = {
    val config = sinkParam.getConfig
    val sinkType = sinkParam.getType
    val sinkTry = sinkType match {
      case Console => Try(ConsoleSink(config, jobName, timeStamp))
      case Hdfs => Try(HdfsSink(config, jobName, timeStamp))
      case ElasticSearch => Try(ElasticSearchSink(config, jobName, timeStamp, block))
      case MongoDB => Try(MongoSink(config, jobName, timeStamp, block))
      case Custom => Try(getCustomSink(config, timeStamp, block))
      case _ => throw new Exception(s"sink type $sinkType is not supported!")
    }
    sinkTry match {
      case Success(sink) if sink.validate() => Some(sink)
      case Failure(ex) =>
        error("Failed to get sink", ex)
        None
    }
  }

  /**
   * Creates a custom [[Sink]] using reflection for a provided class name.
   * Refer to measure configuration guide for more information regarding Custom sinks.
   *
   * @throws ClassCastException when the provided class name does not extend [[Sink]]
   * @param config values defined in Env Config for the custom sink
   * @param timeStamp epoch timestamp
   * @param block persist in blocking or non-blocking way
   * @return [[Sink]] if created successfully
   *
   */
  private def getCustomSink(config: Map[String, Any], timeStamp: Long, block: Boolean): Sink = {
    val className = config.getString("class", "")
    val cls = Class.forName(className)
    if (classOf[Sink].isAssignableFrom(cls)) {
      val method = cls.getDeclaredMethod(
        "apply",
        classOf[Map[String, Any]],
        classOf[String],
        classOf[Long],
        classOf[Boolean])
      method
        .invoke(
          null,
          config,
          jobName.asInstanceOf[Object],
          timeStamp.asInstanceOf[Object],
          block.asInstanceOf[Object])
        .asInstanceOf[Sink]
    } else {
      throw new ClassCastException(s"$className should extend Sink")
    }
  }

}
