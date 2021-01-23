
package com.stackstech.honeybee.bees.datasource.cache

import org.apache.spark.sql._

import com.stackstech.honeybee.bees.datasource.TimestampStorage

/**
 * data source cache in orc format
 */
case class StreamingCacheOrcClient(
    sparkSession: SparkSession,
    param: Map[String, Any],
    dsName: String,
    index: Int,
    timestampStorage: TimestampStorage)
    extends StreamingCacheClient {

  protected def writeDataFrame(dfw: DataFrameWriter[Row], path: String): Unit = {
    info(s"write path: $path")
    dfw.orc(path)
  }

  protected def readDataFrame(dfr: DataFrameReader, path: String): DataFrame = {
    dfr.orc(path)
  }

}
