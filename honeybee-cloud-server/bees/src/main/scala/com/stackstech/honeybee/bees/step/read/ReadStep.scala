
package com.stackstech.honeybee.bees.step.read

import org.apache.spark.sql._
import scala.util.Try

import com.stackstech.honeybee.bees.context.DQContext
import com.stackstech.honeybee.bees.step.DQStep

trait ReadStep extends DQStep {

  val config: Map[String, Any]

  val cache: Boolean

  def execute(context: DQContext): Try[Boolean] = Try {
    info(s"read data source [$name]")
    read(context) match {
      case Some(df) =>
//        if (needCache) context.dataFrameCache.cacheDataFrame(name, df)
        context.runTimeTableRegister.registerTable(name, df)
        true
      case _ =>
        warn(s"read data source [$name] fails")
        false
    }
  }

  def read(context: DQContext): Option[DataFrame]

}
