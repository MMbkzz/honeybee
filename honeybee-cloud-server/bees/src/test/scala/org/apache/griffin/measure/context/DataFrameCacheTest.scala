
package com.stackstech.honeybee.bees.context

import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.types._
import org.scalatest._

import com.stackstech.honeybee.bees.SparkSuiteBase

class DataFrameCacheTest extends FlatSpec with Matchers with SparkSuiteBase {

  def createDataFrame(arr: Seq[Int]): DataFrame = {
    val schema = StructType(
      Array(
        StructField("id", LongType),
        StructField("name", StringType),
        StructField("age", IntegerType)))
    val rows = arr.map { i =>
      Row(i.toLong, s"name_$i", i + 15)
    }
    val rowRdd = spark.sparkContext.parallelize(rows)
    spark.createDataFrame(rowRdd, schema)
  }

  "data frame cache" should "be able to cache and uncache data frames" in {
    val dfCache = DataFrameCache()
    val df1 = createDataFrame(1 to 5)
    val df2 = createDataFrame(1 to 10)
    val df3 = createDataFrame(1 to 15)

    // cache
    dfCache.cacheDataFrame("t1", df1)
    dfCache.cacheDataFrame("t2", df2)
    dfCache.cacheDataFrame("t3", df3)
    dfCache.dataFrames.get("t2") should be(Some(df2))

    // uncache
    dfCache.uncacheDataFrame("t2")
    dfCache.dataFrames.get("t2") should be(None)
    dfCache.trashDataFrames.toList should be(df2 :: Nil)

    // uncache all
    dfCache.uncacheAllDataFrames()
    dfCache.dataFrames.toMap should be(Map[String, DataFrame]())
    dfCache.trashDataFrames.toList should be(df2 :: df1 :: df3 :: Nil)

    // clear all trash
    dfCache.clearAllTrashDataFrames()
    dfCache.trashDataFrames.toList should be(Nil)
  }

}
