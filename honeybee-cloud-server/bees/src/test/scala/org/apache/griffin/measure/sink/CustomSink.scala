
package com.stackstech.honeybee.bees.sink

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.DataFrame

/**
 * sink records and metrics in memory for test.
 *
 * @param config sink configurations
 * @param jobName
 * @param timeStamp
 * @param block
 */
case class CustomSink(config: Map[String, Any], jobName: String, timeStamp: Long, block: Boolean)
    extends Sink {
  def validate(): Boolean = true

  def log(rt: Long, msg: String): Unit = {}

  val allRecords: ListBuffer[String] = mutable.ListBuffer[String]()

  override def sinkRecords(records: RDD[String], name: String): Unit = {
    allRecords ++= records.collect()
  }

  override def sinkRecords(records: Iterable[String], name: String): Unit = {
    allRecords ++= records
  }

  val allMetrics: mutable.Map[String, Any] = mutable.Map[String, Any]()

  override def sinkMetrics(metrics: Map[String, Any]): Unit = {
    allMetrics ++= metrics
  }

  override def sinkBatchRecords(dataset: DataFrame, key: Option[String] = None): Unit = {
    allRecords ++= dataset.toJSON.rdd.collect()
  }
}
