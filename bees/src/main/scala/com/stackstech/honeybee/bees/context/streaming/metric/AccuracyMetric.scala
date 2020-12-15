
package com.stackstech.honeybee.bees.context.streaming.metric

/**
 * accuracy metric
 * @param miss     miss count
 * @param total    total count
 */
case class AccuracyMetric(miss: Long, total: Long) extends Metric {

  type T = AccuracyMetric

  override def isLegal: Boolean = getTotal > 0

  def update(delta: T): T = {
    if (delta.miss < miss) AccuracyMetric(delta.miss, total) else this
  }

  def initial(): Boolean = {
    getMatch <= 0
  }

  def eventual(): Boolean = {
    this.miss <= 0
  }

  def differsFrom(other: T): Boolean = {
    (this.miss != other.miss) || (this.total != other.total)
  }

  def getMiss: Long = miss

  def getTotal: Long = total

  def getMatch: Long = total - miss

  def matchFraction: Double = if (getTotal <= 0) 1 else getMatch.toDouble / getTotal

  def matchPercentage: Double = matchFraction * 100

}
