
package com.stackstech.honeybee.bees.launch.batch

import java.util.concurrent.TimeUnit

import scala.util.Try

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

import com.stackstech.honeybee.bees.configuration.dqdefinition._
import com.stackstech.honeybee.bees.configuration.enums.ProcessType.BatchProcessType
import com.stackstech.honeybee.bees.context._
import com.stackstech.honeybee.bees.datasource.DataSourceFactory
import com.stackstech.honeybee.bees.job.builder.DQJobBuilder
import com.stackstech.honeybee.bees.launch.DQApp
import com.stackstech.honeybee.bees.step.builder.udf.GriffinUDFAgent
import com.stackstech.honeybee.bees.utils.CommonUtils

case class BatchDQApp(allParam: GriffinConfig) extends DQApp {

  val envParam: EnvConfig = allParam.getEnvConfig
  val dqParam: DQConfig = allParam.getDqConfig

  val sparkParam: SparkParam = envParam.getSparkParam
  val metricName: String = dqParam.getName
  val sinkParams: Seq[SinkParam] = getSinkParams

  var dqContext: DQContext = _

  def retryable: Boolean = false

  def init: Try[_] = Try {
    // build spark 2.0+ application context
    val conf = new SparkConf().setAppName(metricName)
    conf.setAll(sparkParam.getConfig)
    conf.set("spark.sql.crossJoin.enabled", "true")
    sparkSession = SparkSession.builder().config(conf).enableHiveSupport().getOrCreate()
    val logLevel = getGriffinLogLevel
    sparkSession.sparkContext.setLogLevel(sparkParam.getLogLevel)
    griffinLogger.setLevel(logLevel)

    // register udf
    GriffinUDFAgent.register(sparkSession)
  }

  def run: Try[Boolean] = {
    val result = CommonUtils.timeThis({
      val measureTime = getMeasureTime
      val contextId = ContextId(measureTime)

      // get data sources
      val dataSources =
        DataSourceFactory.getDataSources(sparkSession, null, dqParam.getDataSources)
      dataSources.foreach(_.init())

      // create dq context
      dqContext =
        DQContext(contextId, metricName, dataSources, sinkParams, BatchProcessType)(sparkSession)

      // start id
      val applicationId = sparkSession.sparkContext.applicationId
      dqContext.getSinks.foreach(_.open(applicationId))

      // build job
      val dqJob = DQJobBuilder.buildDQJob(dqContext, dqParam.getEvaluateRule)

      // dq job execute
      dqJob.execute(dqContext)
    }, TimeUnit.MILLISECONDS)

    // clean context
    dqContext.clean()

    // finish
    dqContext.getSinks.foreach(_.close())

    result
  }

  def close: Try[_] = Try {
    sparkSession.stop()
  }

}
