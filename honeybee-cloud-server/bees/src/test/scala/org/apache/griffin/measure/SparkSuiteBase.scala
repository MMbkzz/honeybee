
package com.stackstech.honeybee.bees

import java.io.File

import org.apache.commons.io.FileUtils
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession
import org.scalatest.{BeforeAndAfterAll, FlatSpec}

trait SparkSuiteBase extends FlatSpec with BeforeAndAfterAll {

  @transient var spark: SparkSession = _
  @transient var sc: SparkContext = _
  @transient var conf: SparkConf = _

  override def beforeAll() {
    super.beforeAll()
    cleanTestHiveData()
    conf = new SparkConf(false)
    spark = SparkSession.builder
      .master("local[4]")
      .appName("Griffin Job Suite")
      .config(conf)
      .enableHiveSupport()
      .getOrCreate()
    sc = spark.sparkContext
  }

  override def afterAll() {
    try {
      spark.sparkContext.stop()
      SparkSession.clearActiveSession()
      if (spark != null) {
        spark.stop()
      }
      spark = null
      if (sc != null) {
        sc.stop()
      }
      sc = null
      conf = null

      cleanTestHiveData()
    } finally {
      super.afterAll()
    }
  }

  def cleanTestHiveData(): Unit = {
    val metastoreDB = new File("metastore_db")
    if (metastoreDB.exists) {
      FileUtils.forceDelete(metastoreDB)
    }
    val sparkWarehouse = new File("spark-warehouse")
    if (sparkWarehouse.exists) {
      FileUtils.forceDelete(sparkWarehouse)
    }
  }
}
