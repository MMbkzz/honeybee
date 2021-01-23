
package com.stackstech.honeybee.bees.datasource.connector.streaming

import kafka.serializer.StringDecoder
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, _}
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka.KafkaUtils

import com.stackstech.honeybee.bees.configuration.dqdefinition.DataConnectorParam
import com.stackstech.honeybee.bees.datasource.TimestampStorage
import com.stackstech.honeybee.bees.datasource.cache.StreamingCacheClient

/**
 * streaming data connector for kafka with string format key and value
 */
case class KafkaStreamingStringDataConnector(
    @transient sparkSession: SparkSession,
    @transient ssc: StreamingContext,
    dcParam: DataConnectorParam,
    timestampStorage: TimestampStorage,
    streamingCacheClientOpt: Option[StreamingCacheClient])
    extends KafkaStreamingDataConnector {

  type K = String
  type KD = StringDecoder
  type V = String
  type VD = StringDecoder

  val valueColName = "value"
  val schema: StructType = StructType(Array(StructField(valueColName, StringType)))

  def createDStream(topicSet: Set[String]): InputDStream[OUT] = {
    KafkaUtils.createDirectStream[K, V, KD, VD](ssc, kafkaConfig, topicSet)
  }

  def transform(rdd: RDD[OUT]): Option[DataFrame] = {
    if (rdd.isEmpty) None
    else {
      try {
        val rowRdd = rdd.map(d => Row(d._2))
        val df = sparkSession.createDataFrame(rowRdd, schema)
        Some(df)
      } catch {
        case _: Throwable =>
          error("streaming data transform fails")
          None
      }
    }
  }

}
