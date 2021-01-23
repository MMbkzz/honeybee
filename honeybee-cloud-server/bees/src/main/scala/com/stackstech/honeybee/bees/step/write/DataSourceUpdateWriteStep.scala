
package com.stackstech.honeybee.bees.step.write

import org.apache.commons.lang.StringUtils
import org.apache.spark.sql.DataFrame
import scala.util.Try

import com.stackstech.honeybee.bees.context.DQContext

/**
 * update data source streaming cache
 */
case class DataSourceUpdateWriteStep(dsName: String, inputName: String) extends WriteStep {

  val name: String = ""
  val writeTimestampOpt: Option[Long] = None

  def execute(context: DQContext): Try[Boolean] = Try {
    getDataSourceCacheUpdateDf(context) match {
      case Some(df) =>
        context.dataSources
          .find(ds => StringUtils.equals(ds.name, dsName))
          .foreach(_.updateData(df))
      case _ =>
        warn(s"update $dsName from $inputName fails")
    }
    true
  }

  private def getDataFrame(context: DQContext, name: String): Option[DataFrame] = {
    try {
      val df = context.sparkSession.table(s"`$name`")
      Some(df)
    } catch {
      case e: Throwable =>
        error(s"get data frame $name fails", e)
        None
    }
  }

  private def getDataSourceCacheUpdateDf(context: DQContext): Option[DataFrame] =
    getDataFrame(context, inputName)

}
