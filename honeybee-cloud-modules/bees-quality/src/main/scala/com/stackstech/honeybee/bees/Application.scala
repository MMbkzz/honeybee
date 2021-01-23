
package com.stackstech.honeybee.bees

import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}

import com.stackstech.honeybee.bees.configuration.dqdefinition.{
  DQConfig,
  EnvConfig,
  GriffinConfig,
  Param
}
import com.stackstech.honeybee.bees.configuration.dqdefinition.reader.ParamReaderFactory
import com.stackstech.honeybee.bees.configuration.enums.ProcessType
import com.stackstech.honeybee.bees.configuration.enums.ProcessType._
import com.stackstech.honeybee.bees.launch.DQApp
import com.stackstech.honeybee.bees.launch.batch.BatchDQApp
import com.stackstech.honeybee.bees.launch.streaming.StreamingDQApp

/**
 * application entrance
 */
object Application extends Loggable {

  def main(args: Array[String]): Unit = {
    info(args.toString)
    if (args.length < 2) {
      error("Usage: class <env-param> <dq-param>")
      sys.exit(-1)
    }

    val envParamFile = args(0)
    val dqParamFile = args(1)

    info(envParamFile)
    info(dqParamFile)

    // read param files
    val envParam = readParamFile[EnvConfig](envParamFile) match {
      case Success(p) => p
      case Failure(ex) =>
        error(ex.getMessage, ex)
        sys.exit(-2)
    }
    val dqParam = readParamFile[DQConfig](dqParamFile) match {
      case Success(p) => p
      case Failure(ex) =>
        error(ex.getMessage, ex)
        sys.exit(-2)
    }
    val allParam: GriffinConfig = GriffinConfig(envParam, dqParam)

    // choose process
    val procType = ProcessType.withNameWithDefault(allParam.getDqConfig.getProcType)
    val dqApp: DQApp = procType match {
      case BatchProcessType => BatchDQApp(allParam)
      case StreamingProcessType => StreamingDQApp(allParam)
      case _ =>
        error(s"$procType is unsupported process type!")
        sys.exit(-4)
    }

    startup()

    // dq app init
    dqApp.init match {
      case Success(_) =>
        info("process init success")
      case Failure(ex) =>
        error(s"process init error: ${ex.getMessage}", ex)
        shutdown()
        sys.exit(-5)
    }

    // dq app run
    val success = dqApp.run match {
      case Success(result) =>
        info("process run result: " + (if (result) "success" else "failed"))
        result

      case Failure(ex) =>
        error(s"process run error: ${ex.getMessage}", ex)

        if (dqApp.retryable) {
          throw ex
        } else {
          shutdown()
          sys.exit(-5)
        }
    }

    // dq app end
    dqApp.close match {
      case Success(_) =>
        info("process end success")
      case Failure(ex) =>
        error(s"process end error: ${ex.getMessage}", ex)
        shutdown()
        sys.exit(-5)
    }

    shutdown()

    if (!success) {
      sys.exit(-5)
    }
  }

  def readParamFile[T <: Param](file: String)(implicit m: ClassTag[T]): Try[T] = {
    val paramReader = ParamReaderFactory.getParamReader(file)
    paramReader.readConfig[T]
  }

  private def startup(): Unit = {}

  private def shutdown(): Unit = {}

}
