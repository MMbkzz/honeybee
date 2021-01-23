
package com.stackstech.honeybee.bees.datasource.connector

import scala.util.Try
import scala.util.matching.Regex

import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.StreamingContext

import com.stackstech.honeybee.bees.Loggable
import com.stackstech.honeybee.bees.configuration.dqdefinition.DataConnectorParam
import com.stackstech.honeybee.bees.datasource.TimestampStorage
import com.stackstech.honeybee.bees.datasource.cache.StreamingCacheClient
import com.stackstech.honeybee.bees.datasource.connector.batch._
import com.stackstech.honeybee.bees.datasource.connector.streaming._

object DataConnectorFactory extends Loggable {

  @deprecated val AvroRegex: Regex = """^(?i)avro$""".r
  @deprecated val TextDirRegex: Regex = """^(?i)text-dir$""".r

  val HiveRegex: Regex = """^(?i)hive$""".r
  val FileRegex: Regex = """^(?i)file$""".r
  val KafkaRegex: Regex = """^(?i)kafka$""".r
  val JDBCRegex: Regex = """^(?i)jdbc$""".r
  val CustomRegex: Regex = """^(?i)custom$""".r
  val ElasticSearchRegex: Regex = """^(?i)elasticsearch$""".r

  /**
   * create data connector
   * @param sparkSession     spark env
   * @param ssc              spark streaming env
   * @param dcParam          data connector param
   * @param tmstCache        same tmst cache in one data source
   * @param streamingCacheClientOpt   for streaming cache
   * @return   data connector
   */
  def getDataConnector(
      sparkSession: SparkSession,
      ssc: StreamingContext,
      dcParam: DataConnectorParam,
      tmstCache: TimestampStorage,
      streamingCacheClientOpt: Option[StreamingCacheClient]): Try[DataConnector] = {
    val conType = dcParam.getType
    Try {
      conType match {
        case HiveRegex() => HiveBatchDataConnector(sparkSession, dcParam, tmstCache)
        case AvroRegex() => AvroBatchDataConnector(sparkSession, dcParam, tmstCache)
        case FileRegex() => FileBasedDataConnector(sparkSession, dcParam, tmstCache)
        case TextDirRegex() => TextDirBatchDataConnector(sparkSession, dcParam, tmstCache)
        case ElasticSearchRegex() => ElasticSearchDataConnector(sparkSession, dcParam, tmstCache)
        case CustomRegex() =>
          getCustomConnector(sparkSession, ssc, dcParam, tmstCache, streamingCacheClientOpt)
        case KafkaRegex() =>
          getStreamingDataConnector(
            sparkSession,
            ssc,
            dcParam,
            tmstCache,
            streamingCacheClientOpt)
        case JDBCRegex() => JDBCBasedDataConnector(sparkSession, dcParam, tmstCache)
        case _ => throw new Exception("connector creation error!")
      }
    }
  }

  private def getStreamingDataConnector(
      sparkSession: SparkSession,
      ssc: StreamingContext,
      dcParam: DataConnectorParam,
      tmstCache: TimestampStorage,
      streamingCacheClientOpt: Option[StreamingCacheClient]): StreamingDataConnector = {
    if (ssc == null) throw new Exception("streaming context is null!")
    val conType = dcParam.getType
    conType match {
      case KafkaRegex() =>
        getKafkaDataConnector(sparkSession, ssc, dcParam, tmstCache, streamingCacheClientOpt)
      case _ => throw new Exception("streaming connector creation error!")
    }
  }

  private def getCustomConnector(
      sparkSession: SparkSession,
      ssc: StreamingContext,
      dcParam: DataConnectorParam,
      timestampStorage: TimestampStorage,
      streamingCacheClientOpt: Option[StreamingCacheClient]): DataConnector = {
    val className = dcParam.getConfig("class").asInstanceOf[String]
    val cls = Class.forName(className)
    if (classOf[BatchDataConnector].isAssignableFrom(cls)) {
      val method = cls.getDeclaredMethod(
        "apply",
        classOf[SparkSession],
        classOf[DataConnectorParam],
        classOf[TimestampStorage])
      method
        .invoke(null, sparkSession, dcParam, timestampStorage)
        .asInstanceOf[BatchDataConnector]
    } else if (classOf[StreamingDataConnector].isAssignableFrom(cls)) {
      val method = cls.getDeclaredMethod(
        "apply",
        classOf[SparkSession],
        classOf[StreamingContext],
        classOf[DataConnectorParam],
        classOf[TimestampStorage],
        classOf[Option[StreamingCacheClient]])
      method
        .invoke(null, sparkSession, ssc, dcParam, timestampStorage, streamingCacheClientOpt)
        .asInstanceOf[StreamingDataConnector]
    } else {
      throw new ClassCastException(
        s"$className should extend BatchDataConnector or StreamingDataConnector")
    }
  }

  private def getKafkaDataConnector(
      sparkSession: SparkSession,
      ssc: StreamingContext,
      dcParam: DataConnectorParam,
      tmstCache: TimestampStorage,
      streamingCacheClientOpt: Option[StreamingCacheClient]): KafkaStreamingDataConnector = {
    val KeyType = "key.type"
    val ValueType = "value.type"
    val config = dcParam.getConfig
    val keyType = config.getOrElse(KeyType, "java.lang.String").toString
    val valueType = config.getOrElse(ValueType, "java.lang.String").toString

    (keyType, valueType) match {
      case ("java.lang.String", "java.lang.String") =>
        KafkaStreamingStringDataConnector(
          sparkSession,
          ssc,
          dcParam,
          tmstCache,
          streamingCacheClientOpt)
      case _ =>
        throw new Exception("not supported type kafka data connector")
    }
  }

}
