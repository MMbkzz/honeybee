
package com.stackstech.honeybee.bees.datasource

import org.scalatest._

class TimestampStorageTest extends FlatSpec with Matchers {

  "timestamp storage" should "be able to insert a timestamp" in {
    val timestampStorage = TimestampStorage()
    timestampStorage.insert(1L)
    timestampStorage.all should be(Set(1L))
  }

  it should "be able to insert timestamps" in {
    val timestampStorage = TimestampStorage()
    timestampStorage.insert(Seq(1L, 2L, 3L))
    timestampStorage.all should be(Set(1L, 2L, 3L))
  }

  it should "be able to remove a timestamp" in {
    val timestampStorage = TimestampStorage()
    timestampStorage.insert(Seq(1L, 2L, 3L))
    timestampStorage.remove(1L)
    timestampStorage.all should be(Set(2L, 3L))
  }

  it should "be able to remove timestamps" in {
    val timestampStorage = TimestampStorage()
    timestampStorage.insert(Seq(1L, 2L, 3L))
    timestampStorage.remove(Seq(1L, 2L))
    timestampStorage.all should be(Set(3L))
  }

  it should "be able to get timestamps in range [a, b)" in {
    val timestampStorage = TimestampStorage()
    timestampStorage.insert(Seq(6L, 2L, 3L, 4L, 8L))
    timestampStorage.fromUntil(2L, 6L) should be(Set(2L, 3L, 4L))
  }

  it should "be able to get timestamps in range (a, b]" in {
    val timestampStorage = TimestampStorage()
    timestampStorage.insert(Seq(6L, 2L, 3L, 4L, 8L))
    timestampStorage.afterTil(2L, 6L) should be(Set(3L, 4L, 6L))
  }

  it should "be able to get timestamps smaller than b" in {
    val timestampStorage = TimestampStorage()
    timestampStorage.insert(Seq(6L, 2L, 3L, 4L, 8L))
    timestampStorage.until(8L) should be(Set(2L, 3L, 4L, 6L))
  }

  it should "be able to get timestamps bigger than or equal a" in {
    val timestampStorage = TimestampStorage()
    timestampStorage.insert(Seq(6L, 2L, 3L, 4L, 8L))
    timestampStorage.from(4L) should be(Set(4L, 6L, 8L))
  }

}
