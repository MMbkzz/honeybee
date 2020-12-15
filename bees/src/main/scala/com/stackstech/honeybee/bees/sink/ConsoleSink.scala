
package com.stackstech.honeybee.bees.sink

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.DataFrame

import com.stackstech.honeybee.bees.utils.JsonUtil
import com.stackstech.honeybee.bees.utils.ParamUtil._

/**
 * Console Sink for Records and Metrics.
 * Records are shown in a tabular structure and Metrics are logged as JSON string.
 *
 * Supported Configurations:
 *  - truncate : [[Boolean]] Whether truncate long strings. If true, strings more than 20 characters
 *  will be truncated and all cells will be aligned right. Default is true.
 *  - numRows : [[Int]] Number of rows to show. Default is 20.
 */
case class ConsoleSink(config: Map[String, Any], jobName: String, timeStamp: Long) extends Sink {

  val block: Boolean = true

  val Truncate: String = "truncate"
  val truncateRecords: Boolean = config.getBoolean(Truncate, defValue = true)

  val NumberOfRows: String = "numRows"
  val numRows: Int = config.getInt(NumberOfRows, 20)

  def validate(): Boolean = true

  override def open(applicationId: String): Unit = {
    griffinLogger.info(
      s"Opened ConsoleSink for job with name '$jobName', " +
        s"timestamp '$timeStamp' and applicationId '$applicationId'")
  }

  override def close(): Unit = {
    griffinLogger.info(
      s"Closed ConsoleSink for job with name '$jobName' and timestamp '$timeStamp'")
  }

  override def sinkRecords(records: RDD[String], name: String): Unit = {}

  override def sinkRecords(records: Iterable[String], name: String): Unit = {}

  override def sinkMetrics(metrics: Map[String, Any]): Unit = {
    griffinLogger.info(s"$jobName [$timeStamp] metrics:\n${JsonUtil.toJson(metrics)}")
  }

  override def sinkBatchRecords(dataset: DataFrame, key: Option[String] = None): Unit = {
    dataset.show(numRows, truncateRecords)
  }

}
