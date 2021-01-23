
package com.stackstech.honeybee.bees.context.streaming.checkpoint.offset

import scala.util.Try
import scala.util.matching.Regex

import com.stackstech.honeybee.bees.configuration.dqdefinition.CheckpointParam

case class OffsetCheckpointFactory(
    checkpointParams: Iterable[CheckpointParam],
    metricName: String)
    extends Serializable {

  val ZK_REGEX: Regex = """^(?i)zk|zookeeper$""".r

  def getOffsetCheckpoint(checkpointParam: CheckpointParam): Option[OffsetCheckpoint] = {
    val config = checkpointParam.getConfig
    val offsetCheckpointTry = checkpointParam.getType match {
      case ZK_REGEX() => Try(OffsetCheckpointInZK(config, metricName))
      case _ => throw new Exception("not supported info cache type")
    }
    offsetCheckpointTry.toOption
  }

}
