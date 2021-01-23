
package com.stackstech.honeybee.bees.step.transform

import scala.util.Try

import com.stackstech.honeybee.bees.context.DQContext
import com.stackstech.honeybee.bees.step.write.WriteStep

/**
 * data frame ops transform step
 */
case class DataFrameOpsTransformStep[T <: WriteStep](
    name: String,
    inputDfName: String,
    rule: String,
    details: Map[String, Any],
    writeStepOpt: Option[T] = None,
    cache: Boolean = false)
    extends TransformStep {

  def doExecute(context: DQContext): Try[Boolean] =
    Try {
      val sparkSession = context.sparkSession
      val df = rule match {
        case DataFrameOps._fromJson => DataFrameOps.fromJson(sparkSession, inputDfName, details)
        case DataFrameOps._accuracy =>
          DataFrameOps.accuracy(sparkSession, inputDfName, context.contextId, details)
        case DataFrameOps._clear => DataFrameOps.clear(sparkSession, inputDfName, details)
        case _ => throw new Exception(s"df opr [ $rule ] not supported")
      }
      if (cache) context.dataFrameCache.cacheDataFrame(name, df)
      context.runTimeTableRegister.registerTable(name, df)
      writeStepOpt match {
        case Some(writeStep) => writeStep.execute(context)
        case None => Try(true)
      }
    }.flatten

}
