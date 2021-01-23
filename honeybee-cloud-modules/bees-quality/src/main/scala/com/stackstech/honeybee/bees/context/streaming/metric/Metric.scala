
package com.stackstech.honeybee.bees.context.streaming.metric

trait Metric extends Serializable {

  type T <: Metric

  def isLegal: Boolean = true

  def update(delta: T): T

  def initial(): Boolean

  def eventual(): Boolean

  def differsFrom(other: T): Boolean

}
