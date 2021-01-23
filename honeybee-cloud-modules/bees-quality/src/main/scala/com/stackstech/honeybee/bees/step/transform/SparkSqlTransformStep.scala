
package com.stackstech.honeybee.bees.step.transform

import scala.util.Try

import com.stackstech.honeybee.bees.context.DQContext
import com.stackstech.honeybee.bees.step.write.WriteStep

/**
 * spark sql transform step
 */
case class SparkSqlTransformStep[T <: WriteStep](
    name: String,
    rule: String,
    details: Map[String, Any],
    writeStepOpt: Option[T] = None,
    cache: Boolean = false)
    extends TransformStep {

  def doExecute(context: DQContext): Try[Boolean] =
    Try {
      val sparkSession = context.sparkSession
      val df = sparkSession.sql(rule)
      if (cache) context.dataFrameCache.cacheDataFrame(name, df)
      context.runTimeTableRegister.registerTable(name, df)
      writeStepOpt match {
        case Some(writeStep) => writeStep.execute(context)
        case None => Try(true)
      }
    }.flatten

}
