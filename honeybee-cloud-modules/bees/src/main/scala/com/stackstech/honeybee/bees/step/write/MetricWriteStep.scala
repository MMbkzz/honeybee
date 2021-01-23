
package com.stackstech.honeybee.bees.step.write

import scala.util.Try

import com.stackstech.honeybee.bees.configuration.enums.{SimpleMode, TimestampMode}
import com.stackstech.honeybee.bees.configuration.enums.FlattenType.{
  ArrayFlattenType,
  EntriesFlattenType,
  FlattenType,
  MapFlattenType
}
import com.stackstech.honeybee.bees.context.DQContext
import com.stackstech.honeybee.bees.step.builder.ConstantColumns
import com.stackstech.honeybee.bees.utils.JsonUtil
import com.stackstech.honeybee.bees.utils.ParamUtil._

/**
 * write metrics into context metric wrapper
 */
case class MetricWriteStep(
    name: String,
    inputName: String,
    flattenType: FlattenType,
    writeTimestampOpt: Option[Long] = None)
    extends WriteStep {

  val emptyMetricMap: Map[Long, Map[String, Any]] = Map[Long, Map[String, Any]]()
  val emptyMap: Map[String, Any] = Map[String, Any]()

  def execute(context: DQContext): Try[Boolean] = Try {
    val timestamp = writeTimestampOpt.getOrElse(context.contextId.timestamp)

    // get metric list from data frame
    val metricMaps: Seq[Map[String, Any]] = getMetricMaps(context)

    // get timestamp and normalize metric
    val writeMode = writeTimestampOpt.map(_ => SimpleMode).getOrElse(context.writeMode)
    val timestampMetricMap: Map[Long, Map[String, Any]] = writeMode match {

      case SimpleMode =>
        val metrics: Map[String, Any] = flattenMetric(metricMaps, name, flattenType)
        emptyMetricMap + (timestamp -> metrics)

      case TimestampMode =>
        val tmstMetrics = metricMaps.map { metric =>
          val tmst = metric.getLong(ConstantColumns.tmst, timestamp)
          val pureMetric = metric.removeKeys(ConstantColumns.columns)
          (tmst, pureMetric)
        }
        tmstMetrics.groupBy(_._1).map { pair =>
          val (k, v) = pair
          val maps = v.map(_._2)
          val mtc = flattenMetric(maps, name, flattenType)
          (k, mtc)
        }
    }

    // write to metric wrapper
    timestampMetricMap.foreach { pair =>
      val (timestamp, v) = pair
      context.metricWrapper.insertMetric(timestamp, v)
    }

    true
  }

  private def getMetricMaps(context: DQContext): Seq[Map[String, Any]] = {
    try {
      val pdf = context.sparkSession.table(s"`$inputName`")
      val rows = pdf.collect()
      val columns = pdf.columns
      if (rows.size > 0) {
        rows.map(_.getValuesMap(columns))
      } else Nil
    } catch {
      case e: Throwable =>
        error(s"get metric $name fails", e)
        Nil
    }
  }

  private def flattenMetric(
      metrics: Seq[Map[String, Any]],
      name: String,
      flattenType: FlattenType): Map[String, Any] = {
    flattenType match {
      case EntriesFlattenType => metrics.headOption.getOrElse(emptyMap)
      case ArrayFlattenType => Map[String, Any](name -> metrics)
      case MapFlattenType =>
        val v = metrics.headOption.getOrElse(emptyMap)
        Map[String, Any](name -> v)
      case _ =>
        if (metrics.size > 1) Map[String, Any](name -> metrics)
        else metrics.headOption.getOrElse(emptyMap)
    }
  }

}
