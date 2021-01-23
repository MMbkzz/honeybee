
package com.stackstech.honeybee.bees.datasource.cache

import scala.util.matching.Regex

import org.apache.spark.sql.SparkSession

import com.stackstech.honeybee.bees.Loggable
import com.stackstech.honeybee.bees.datasource.TimestampStorage
import com.stackstech.honeybee.bees.utils.ParamUtil._

object StreamingCacheClientFactory extends Loggable {

  private object DataSourceCacheType {
    val ParquetRegex: Regex = "^(?i)parq(uet)?$".r
    val JsonRegex: Regex = "^(?i)json$".r
    val OrcRegex: Regex = "^(?i)orc$".r
  }
  import DataSourceCacheType._

  val _type = "type"

  /**
   * create streaming cache client
   * @param sparkSession     sparkSession in spark environment
   * @param checkpointOpt  data source checkpoint/cache config option
   * @param name           data source name
   * @param index          data source index
   * @param tmstCache      the same tmstCache instance inside a data source
   * @return               streaming cache client option
   */
  def getClientOpt(
      sparkSession: SparkSession,
      checkpointOpt: Option[Map[String, Any]],
      name: String,
      index: Int,
      tmstCache: TimestampStorage): Option[StreamingCacheClient] = {
    checkpointOpt.flatMap { param =>
      try {
        val tp = param.getString(_type, "")
        val dsCache = tp match {
          case ParquetRegex() =>
            StreamingCacheParquetClient(sparkSession, param, name, index, tmstCache)
          case JsonRegex() =>
            StreamingCacheJsonClient(sparkSession, param, name, index, tmstCache)
          case OrcRegex() =>
            StreamingCacheOrcClient(sparkSession, param, name, index, tmstCache)
          case _ =>
            StreamingCacheParquetClient(sparkSession, param, name, index, tmstCache)
        }
        Some(dsCache)
      } catch {
        case _: Throwable =>
          error("generate data source cache fails")
          None
      }
    }
  }

}
