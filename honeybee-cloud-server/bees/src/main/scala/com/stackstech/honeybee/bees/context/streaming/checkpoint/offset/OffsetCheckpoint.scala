
package com.stackstech.honeybee.bees.context.streaming.checkpoint.offset

import com.stackstech.honeybee.bees.Loggable
import com.stackstech.honeybee.bees.context.streaming.checkpoint.lock.CheckpointLock

trait OffsetCheckpoint extends Loggable with Serializable {

  def init(): Unit
  def available(): Boolean
  def close(): Unit

  def cache(kvs: Map[String, String]): Unit
  def read(keys: Iterable[String]): Map[String, String]
  def delete(keys: Iterable[String]): Unit
  def clear(): Unit

  def listKeys(path: String): List[String]

  def genLock(s: String): CheckpointLock

}
