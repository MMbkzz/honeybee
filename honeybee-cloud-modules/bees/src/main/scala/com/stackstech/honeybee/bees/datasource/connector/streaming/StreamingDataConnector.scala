
package com.stackstech.honeybee.bees.datasource.connector.streaming

import scala.util.Try

import org.apache.spark.rdd.RDD
import org.apache.spark.sql._
import org.apache.spark.streaming.dstream.InputDStream

import com.stackstech.honeybee.bees.context.TimeRange
import com.stackstech.honeybee.bees.datasource.cache.StreamingCacheClient
import com.stackstech.honeybee.bees.datasource.connector.DataConnector

trait StreamingDataConnector extends DataConnector {

  type K
  type V
  type OUT

  protected def stream(): Try[InputDStream[OUT]]

  // transform rdd to dataframe
  def transform(rdd: RDD[OUT]): Option[DataFrame]

  // streaming data connector cannot directly read data frame
  def data(ms: Long): (Option[DataFrame], TimeRange) = (None, TimeRange.emptyTimeRange)

  val streamingCacheClientOpt: Option[StreamingCacheClient]

}
