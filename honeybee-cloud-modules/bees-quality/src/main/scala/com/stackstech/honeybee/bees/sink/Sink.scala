
package com.stackstech.honeybee.bees.sink

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.DataFrame

import com.stackstech.honeybee.bees.Loggable

/**
 * Base trait for batch and Streaming Sinks.
 * To implement custom sinks, extend your classes with this trait.
 */
trait Sink extends Loggable with Serializable {

  val jobName: String
  val timeStamp: Long

  val config: Map[String, Any]

  val block: Boolean

  /**
   * Ensures that the pre-requisites (if any) of the Sink are met before opening it.
   */
  def validate(): Boolean

  /**
   * Allows initialization of the connection to the sink (if required).
   *
   * @param applicationId Spark Application ID
   */
  def open(applicationId: String): Unit = {}

  /**
   * Allows clean up for the sink (if required).
   */
  def close(): Unit = {}

  /**
   * Implementation of persisting records for streaming pipelines.
   */
  def sinkRecords(records: RDD[String], name: String): Unit = {}

  /**
   * Implementation of persisting records for streaming pipelines.
   */
  def sinkRecords(records: Iterable[String], name: String): Unit = {}

  /**
   * Implementation of persisting metrics.
   */
  def sinkMetrics(metrics: Map[String, Any]): Unit = {}

  /**
   * Implementation of persisting records for batch pipelines.
   */
  def sinkBatchRecords(dataset: DataFrame, key: Option[String] = None): Unit = {}
}
