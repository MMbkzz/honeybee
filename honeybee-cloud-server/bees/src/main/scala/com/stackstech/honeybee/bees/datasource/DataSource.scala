
package com.stackstech.honeybee.bees.datasource

import org.apache.spark.sql._

import com.stackstech.honeybee.bees.Loggable
import com.stackstech.honeybee.bees.configuration.dqdefinition.DataSourceParam
import com.stackstech.honeybee.bees.context.{DQContext, TimeRange}
import com.stackstech.honeybee.bees.datasource.cache.StreamingCacheClient
import com.stackstech.honeybee.bees.datasource.connector.DataConnector
import com.stackstech.honeybee.bees.utils.DataFrameUtil._

/**
 * data source
 * @param name     name of data source
 * @param dsParam  param of this data source
 * @param dataConnector       data connector
 * @param streamingCacheClientOpt   streaming data cache client option
 */
case class DataSource(
    name: String,
    dsParam: DataSourceParam,
    dataConnector: Option[DataConnector],
    streamingCacheClientOpt: Option[StreamingCacheClient])
    extends Loggable
    with Serializable {

  val isBaseline: Boolean = dsParam.isBaseline

  def init(): Unit = {
    dataConnector.foreach(_.init())
  }

  def loadData(context: DQContext): TimeRange = {
    info(s"load data [$name]")
    try {
      val timestamp = context.contextId.timestamp
      val (dfOpt, timeRange) = data(timestamp)
      dfOpt match {
        case Some(df) =>
          context.runTimeTableRegister.registerTable(name, df)
        case None =>
          warn(s"Data source [$name] is null!")
      }
      timeRange
    } catch {
      case e: Throwable =>
        error(s"load data source [$name] fails")
        throw e
    }
  }

  private def data(timestamp: Long): (Option[DataFrame], TimeRange) = {
    val batches = dataConnector.flatMap { dc =>
      val (dfOpt, timeRange) = dc.data(timestamp)
      dfOpt match {
        case Some(_) => Some((dfOpt, timeRange))
        case _ => None
      }
    }
    val caches = streamingCacheClientOpt match {
      case Some(dsc) => dsc.readData() :: Nil
      case _ => Nil
    }
    val pairs = batches ++ caches

    if (pairs.nonEmpty) {
      pairs.reduce { (a, b) =>
        (unionDfOpts(a._1, b._1), a._2.merge(b._2))
      }
    } else {
      (None, TimeRange.emptyTimeRange)
    }
  }

  def updateData(df: DataFrame): Unit = {
    streamingCacheClientOpt.foreach(_.updateData(Some(df)))
  }

  def cleanOldData(): Unit = {
    streamingCacheClientOpt.foreach(_.cleanOutTimeData())
  }

  def processFinish(): Unit = {
    streamingCacheClientOpt.foreach(_.processFinish())
  }

}
