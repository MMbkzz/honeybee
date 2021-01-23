
package com.stackstech.honeybee.bees.datasource

import scala.collection.mutable.{SortedSet => MutableSortedSet}

import com.stackstech.honeybee.bees.Loggable

/**
 * tmst cache, CRUD of timestamps
 */
case class TimestampStorage() extends Loggable {

  private val tmstGroup: MutableSortedSet[Long] = MutableSortedSet.empty[Long]

  // -- insert tmst into tmst group --
  def insert(tmst: Long): MutableSortedSet[Long] = tmstGroup += tmst
  def insert(tmsts: Iterable[Long]): MutableSortedSet[Long] = tmstGroup ++= tmsts

  // -- remove tmst from tmst group --
  def remove(tmst: Long): MutableSortedSet[Long] = tmstGroup -= tmst
  def remove(tmsts: Iterable[Long]): MutableSortedSet[Long] = tmstGroup --= tmsts

  // -- get subset of tmst group --
  def fromUntil(from: Long, until: Long): Set[Long] = tmstGroup.range(from, until).toSet
  def afterTil(after: Long, til: Long): Set[Long] = tmstGroup.range(after + 1, til + 1).toSet
  def until(until: Long): Set[Long] = tmstGroup.until(until).toSet
  def from(from: Long): Set[Long] = tmstGroup.from(from).toSet
  def all: Set[Long] = tmstGroup.toSet

}
