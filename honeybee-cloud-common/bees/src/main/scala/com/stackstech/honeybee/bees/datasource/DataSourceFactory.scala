
package com.stackstech.honeybee.bees.datasource

import scala.util.Success

import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.StreamingContext

import com.stackstech.honeybee.bees.Loggable
import com.stackstech.honeybee.bees.configuration.dqdefinition.DataSourceParam
import com.stackstech.honeybee.bees.datasource.cache.StreamingCacheClientFactory
import com.stackstech.honeybee.bees.datasource.connector.DataConnectorFactory

object DataSourceFactory extends Loggable {

  def getDataSources(
      sparkSession: SparkSession,
      ssc: StreamingContext,
      dataSources: Seq[DataSourceParam]): Seq[DataSource] = {
    dataSources.zipWithIndex.flatMap { pair =>
      val (param, index) = pair
      getDataSource(sparkSession, ssc, param, index)
    }
  }

  private def getDataSource(
      sparkSession: SparkSession,
      ssc: StreamingContext,
      dataSourceParam: DataSourceParam,
      index: Int): Option[DataSource] = {
    val name = dataSourceParam.getName
    val timestampStorage = TimestampStorage()

    // for streaming data cache
    val streamingCacheClientOpt = StreamingCacheClientFactory.getClientOpt(
      sparkSession,
      dataSourceParam.getCheckpointOpt,
      name,
      index,
      timestampStorage)

    val connectorParamsOpt = dataSourceParam.getConnector

    connectorParamsOpt match {
      case Some(connectorParam) =>
        val dataConnectors = DataConnectorFactory.getDataConnector(
          sparkSession,
          ssc,
          connectorParam,
          timestampStorage,
          streamingCacheClientOpt) match {
          case Success(connector) => Some(connector)
          case _ => None
        }

        Some(DataSource(name, dataSourceParam, dataConnectors, streamingCacheClientOpt))
      case None => None
    }
  }

}
