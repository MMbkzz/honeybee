
package com.stackstech.honeybee.bees.datasource.cache

import org.apache.spark.sql._

import com.stackstech.honeybee.bees.datasource.TimestampStorage

/**
 * data source cache in parquet format
 */
case class StreamingCacheParquetClient(
    sparkSession: SparkSession,
    param: Map[String, Any],
    dsName: String,
    index: Int,
    timestampStorage: TimestampStorage)
    extends StreamingCacheClient {

  sparkSession.sparkContext.hadoopConfiguration.set("parquet.enable.summary-metadata", "false")

  protected def writeDataFrame(dfw: DataFrameWriter[Row], path: String): Unit = {
    info(s"write path: $path")
    dfw.parquet(path)
  }

  protected def readDataFrame(dfr: DataFrameReader, path: String): DataFrame = {
    dfr.parquet(path)
  }

}
