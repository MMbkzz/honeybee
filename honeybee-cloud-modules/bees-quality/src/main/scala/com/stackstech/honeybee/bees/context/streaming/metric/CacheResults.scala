
package com.stackstech.honeybee.bees.context.streaming.metric

import scala.collection.mutable.{Map => MutableMap}

import com.stackstech.honeybee.bees.Loggable

/**
 * in streaming mode, some metrics may update,
 * the old metrics are cached here
 */
object CacheResults extends Loggable {

  case class CacheResult(timeStamp: Long, updateTime: Long, result: Metric) {
    def olderThan(ut: Long): Boolean = updateTime < ut
    def update[A <: result.T: Manifest](ut: Long, r: Metric): Option[Metric] = {
      r match {
        case m: A if olderThan(ut) =>
          val ur = result.update(m)
          Some(ur).filter(result.differsFrom)
        case _ => None
      }
    }
  }

  private val cacheGroup: MutableMap[Long, CacheResult] = MutableMap()

  private def update(r: CacheResult): Unit = {
    cacheGroup += (r.timeStamp -> r)
  }

  /**
   * input new metric results, output the updated metric results.
   */
  def update(cacheResults: Iterable[CacheResult]): Iterable[CacheResult] = {
    val updatedCacheResults = cacheResults.flatMap { cacheResult =>
      val CacheResult(t, ut, r) = cacheResult
      (cacheGroup.get(t) match {
        case Some(cr) => cr.update(ut, r)
        case _ => Some(r)
      }).map(m => CacheResult(t, ut, m))
    }
    updatedCacheResults.foreach(r => update(r))
    updatedCacheResults
  }

  /**
   * clean the out-time cached results, to avoid memory leak
   */
  def refresh(overtime: Long): Unit = {
    val curCacheGroup = cacheGroup.toMap
    val deadCache = curCacheGroup.filter { pr =>
      val (_, cr) = pr
      cr.timeStamp < overtime || cr.result.eventual()
    }
    info(s"=== dead cache group count: ${deadCache.size} ===")
    deadCache.keySet.foreach(cacheGroup -= _)
  }

}
