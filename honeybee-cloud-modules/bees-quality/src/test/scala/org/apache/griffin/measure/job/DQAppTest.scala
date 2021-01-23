
package com.stackstech.honeybee.bees.job

import scala.util.{Failure, Success}

import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import com.stackstech.honeybee.bees.{Loggable, SparkSuiteBase}
import com.stackstech.honeybee.bees.Application._
import com.stackstech.honeybee.bees.configuration.dqdefinition._
import com.stackstech.honeybee.bees.configuration.enums.ProcessType
import com.stackstech.honeybee.bees.configuration.enums.ProcessType._
import com.stackstech.honeybee.bees.launch.DQApp
import com.stackstech.honeybee.bees.launch.batch.BatchDQApp
import com.stackstech.honeybee.bees.launch.streaming.StreamingDQApp

class DQAppTest
    extends FlatSpec
    with SparkSuiteBase
    with BeforeAndAfterAll
    with Matchers
    with Loggable {

  var envParam: EnvConfig = _
  var sparkParam: SparkParam = _

  var dqApp: DQApp = _

  def getConfigFilePath(fileName: String): String = {
    try {
      getClass.getResource(fileName).getFile
    } catch {
      case _: NullPointerException => throw new Exception(s"resource [$fileName] not found")
      case ex: Throwable => throw ex
    }
  }

  def initApp(dqParamFile: String): DQApp = {
    val dqParam = readParamFile[DQConfig](getConfigFilePath(dqParamFile)) match {
      case Success(p) => p
      case Failure(ex) =>
        error(ex.getMessage, ex)
        sys.exit(-2)
    }

    val allParam: GriffinConfig = GriffinConfig(envParam, dqParam)

    // choose process
    val procType = ProcessType.withNameWithDefault(allParam.getDqConfig.getProcType)
    dqApp = procType match {
      case BatchProcessType => BatchDQApp(allParam)
      case StreamingProcessType => StreamingDQApp(allParam)
      case _ =>
        error(s"$procType is unsupported process type!")
        sys.exit(-4)
    }

    dqApp.sparkSession = spark
    dqApp
  }
}
