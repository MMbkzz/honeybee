
package com.stackstech.honeybee.bees.step.builder.udf

import org.apache.spark.sql.SparkSession

object GriffinUDFAgent {
  def register(sparkSession: SparkSession): Unit = {
    GriffinUDFs.register(sparkSession)
    GriffinUDAggFs.register(sparkSession)
  }
}

/**
 * user defined functions extension
 */
object GriffinUDFs {

  def register(sparkSession: SparkSession): Unit = {
    sparkSession.udf.register("index_of", indexOf _)
    sparkSession.udf.register("matches", matches _)
    sparkSession.udf.register("reg_replace", regReplace _)
  }

  private def indexOf(arr: Seq[String], v: String) = {
    arr.indexOf(v)
  }

  private def matches(s: String, regex: String) = {
    s.matches(regex)
  }

  private def regReplace(s: String, regex: String, replacement: String) = {
    s.replaceAll(regex, replacement)
  }

}

/**
 * aggregation functions extension
 */
object GriffinUDAggFs {

  def register(sparkSession: SparkSession): Unit = {}

}
