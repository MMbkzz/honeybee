
package com.stackstech.honeybee.bees.context.streaming.checkpoint.offset

import com.stackstech.honeybee.bees.configuration.dqdefinition.CheckpointParam
import com.stackstech.honeybee.bees.context.streaming.checkpoint.lock.{
  CheckpointLock,
  CheckpointLockSeq
}

object OffsetCheckpointClient extends OffsetCheckpoint with OffsetOps {
  var offsetCheckpoints: Seq[OffsetCheckpoint] = Nil

  def initClient(checkpointParams: Iterable[CheckpointParam], metricName: String): Unit = {
    val fac = OffsetCheckpointFactory(checkpointParams, metricName)
    offsetCheckpoints = checkpointParams.flatMap(param => fac.getOffsetCheckpoint(param)).toList
  }

  def init(): Unit = offsetCheckpoints.foreach(_.init())
  def available(): Boolean = offsetCheckpoints.foldLeft(false)(_ || _.available)
  def close(): Unit = offsetCheckpoints.foreach(_.close())

  def cache(kvs: Map[String, String]): Unit = {
    offsetCheckpoints.foreach(_.cache(kvs))
  }
  def read(keys: Iterable[String]): Map[String, String] = {
    val maps = offsetCheckpoints.map(_.read(keys)).reverse
    maps.fold(Map[String, String]())(_ ++ _)
  }
  def delete(keys: Iterable[String]): Unit = offsetCheckpoints.foreach(_.delete(keys))
  def clear(): Unit = offsetCheckpoints.foreach(_.clear())

  def listKeys(path: String): List[String] = {
    offsetCheckpoints.foldLeft(Nil: List[String]) { (res, offsetCheckpoint) =>
      if (res.nonEmpty) res else offsetCheckpoint.listKeys(path)
    }
  }

  def genLock(s: String): CheckpointLock = CheckpointLockSeq(offsetCheckpoints.map(_.genLock(s)))

}
