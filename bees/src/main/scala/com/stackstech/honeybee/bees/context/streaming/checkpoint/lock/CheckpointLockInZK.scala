
package com.stackstech.honeybee.bees.context.streaming.checkpoint.lock

import java.util.concurrent.TimeUnit

import org.apache.curator.framework.recipes.locks.InterProcessMutex

case class CheckpointLockInZK(@transient mutex: InterProcessMutex) extends CheckpointLock {

  def lock(outtime: Long, unit: TimeUnit): Boolean = {
    try {
      if (outtime >= 0) {
        mutex.acquire(outtime, unit)
      } else {
        mutex.acquire(-1, null)
      }
    } catch {
      case e: Throwable =>
        error(s"lock error: ${e.getMessage}")
        false
    }

  }

  def unlock(): Unit = {
    try {
      if (mutex.isAcquiredInThisProcess) mutex.release()
    } catch {
      case e: Throwable =>
        error(s"unlock error: ${e.getMessage}")
    }
  }

}
