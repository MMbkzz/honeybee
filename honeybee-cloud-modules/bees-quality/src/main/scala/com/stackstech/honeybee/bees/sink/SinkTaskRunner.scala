
package com.stackstech.honeybee.bees.sink

import java.util.Date
import java.util.concurrent.TimeUnit

import scala.concurrent._
import scala.concurrent.duration._
import scala.util.{Failure, Success}

import com.stackstech.honeybee.bees.Loggable

/**
 * sink task runner, to sink metrics in block or non-block mode
 */
object SinkTaskRunner extends Loggable {

  import scala.concurrent.ExecutionContext.Implicits.global

  val MAX_RETRY = 100

  def addNonBlockTask(func: () => (Long, Future[_]), retry: Int): Unit = {
    val r = validRetryNum(retry)
    nonBlockExecute(func, r)
  }

  def addBlockTask(func: () => (Long, Future[_]), retry: Int, wait: Long): Unit = {
    val r = validRetryNum(retry)
    val duration = if (wait >= 0) Duration(wait, TimeUnit.MILLISECONDS) else Duration.Inf
    blockExecute(func, r, duration)
  }

  private def nonBlockExecute(func: () => (Long, Future[_]), retry: Int): Unit = {
    val nextRetry = nextRetryCount(retry)
    val st = new Date().getTime
    val (t, res) = func()
    res.onComplete {
      case Success(value) =>
        val et = new Date().getTime
        info(s"task $t success with ($value) [ using time ${et - st} ms ]")

      case Failure(e) =>
        val et = new Date().getTime
        warn(s"task $t fails [ using time ${et - st} ms ] : ${e.getMessage}")
        if (nextRetry >= 0) {
          info(s"task $t retry [ rest retry count: $nextRetry ]")
          nonBlockExecute(func, nextRetry)
        } else {
          error(s"task fails: task $t retry ends but fails", e)
        }
    }
  }

  @scala.annotation.tailrec
  private def blockExecute(
      func: () => (Long, Future[_]),
      retry: Int,
      waitDuration: Duration): Unit = {
    val nextRetry = nextRetryCount(retry)
    val st = new Date().getTime
    val (t, res) = func()
    try {
      val value = Await.result(res, waitDuration)
      val et = new Date().getTime
      info(s"task $t success with ($value) [ using time ${et - st} ms ]")
    } catch {
      case e: Throwable =>
        val et = new Date().getTime
        warn(s"task $t fails [ using time ${et - st} ms ] : ${e.getMessage}")
        if (nextRetry >= 0) {
          info(s"task $t retry [ rest retry count: $nextRetry ]")
          blockExecute(func, nextRetry, waitDuration)
        } else {
          error(s"task fails: task $t retry ends but fails", e)
        }
    }
  }

  private def validRetryNum(retry: Int): Int = {
    if (retry > MAX_RETRY) MAX_RETRY else retry
  }
  private def nextRetryCount(retry: Int): Int = {
    if (retry >= 0) retry - 1 else -1
  }

}
