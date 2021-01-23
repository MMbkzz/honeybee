
package com.stackstech.honeybee.bees.context.streaming.checkpoint.lock

import java.util.concurrent.TimeUnit

case class CheckpointLockSeq(locks: Seq[CheckpointLock]) extends CheckpointLock {

  def lock(outtime: Long, unit: TimeUnit): Boolean = {
    locks.headOption.forall(_.lock(outtime, unit))
  }

  def unlock(): Unit = {
    locks.headOption.foreach(_.unlock())
  }

}
