
package com.stackstech.honeybee.bees.sink

import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.types._
import org.scalatest.{FlatSpec, Matchers}

import com.stackstech.honeybee.bees.{Loggable, SparkSuiteBase}
import com.stackstech.honeybee.bees.configuration.dqdefinition.SinkParam
import com.stackstech.honeybee.bees.configuration.enums.ProcessType.BatchProcessType
import com.stackstech.honeybee.bees.context.{ContextId, DQContext}

trait SinkTestBase extends FlatSpec with Matchers with SparkSuiteBase with Loggable {

  var sinkParams: Seq[SinkParam]

  def getDqContext(name: String = "test-context"): DQContext = {
    DQContext(ContextId(System.currentTimeMillis), name, Nil, sinkParams, BatchProcessType)(spark)
  }

  def createDataFrame(arr: Seq[Int]): DataFrame = {
    val schema = StructType(
      Array(
        StructField("id", LongType),
        StructField("name", StringType),
        StructField("sex", StringType),
        StructField("age", IntegerType)))
    val rows = arr.map { i =>
      Row(i.toLong, s"name_$i", if (i % 2 == 0) "man" else "women", i + 15)
    }
    val rowRdd = spark.sparkContext.parallelize(rows)
    spark.createDataFrame(rowRdd, schema)
  }
}
