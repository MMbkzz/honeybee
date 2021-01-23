
package com.stackstech.honeybee.bees.context.streaming.checkpoint.lock

import java.util.concurrent.TimeUnit

import com.stackstech.honeybee.bees.Loggable

/**
 * lock for checkpoint
 */
trait CheckpointLock extends Loggable with Serializable {

  def lock(outtime: Long, unit: TimeUnit): Boolean

  def unlock(): Unit

}
