
package com.stackstech.honeybee.bees.step.write

import scala.util.Try

import org.apache.spark.rdd.RDD
import org.apache.spark.sql._

import com.stackstech.honeybee.bees.configuration.enums._
import com.stackstech.honeybee.bees.context.DQContext
import com.stackstech.honeybee.bees.step.builder.ConstantColumns
import com.stackstech.honeybee.bees.utils.JsonUtil

/**
 * write records needs to be sink
 */
case class RecordWriteStep(
    name: String,
    inputName: String,
    filterTableNameOpt: Option[String] = None,
    writeTimestampOpt: Option[Long] = None)
    extends WriteStep {

  def execute(context: DQContext): Try[Boolean] = Try {
    val timestamp = writeTimestampOpt.getOrElse(context.contextId.timestamp)

    val writeMode = writeTimestampOpt.map(_ => SimpleMode).getOrElse(context.writeMode)
    writeMode match {
      case SimpleMode =>
        // batch records
        val recordsOpt = getBatchRecords(context)
        // write records
        recordsOpt match {
          case Some(records) =>
            context.getSinks(timestamp).foreach { sink =>
              try {
                sink.sinkBatchRecords(records, Option(name))
              } catch {
                case e: Throwable => error(s"sink records error: ${e.getMessage}", e)
              }
            }
          case _ =>
        }
      case TimestampMode =>
        // streaming records
        val (recordsOpt, emptyTimestamps) = getStreamingRecords(context)
        // write records
        recordsOpt.foreach { records =>
          records.foreach { pair =>
            val (t, strRecords) = pair
            context.getSinks(t).foreach { sink =>
              try {
                sink.sinkRecords(strRecords, name)
              } catch {
                case e: Throwable => error(s"sink records error: ${e.getMessage}", e)
              }
            }
          }
        }
        emptyTimestamps.foreach { t =>
          context.getSinks(t).foreach { sink =>
            try {
              sink.sinkRecords(Nil, name)
            } catch {
              case e: Throwable => error(s"sink records error: ${e.getMessage}", e)
            }
          }
        }
    }
    true
  }

  private def getTmst(row: Row, defTmst: Long): Long = {
    try {
      row.getAs[Long](ConstantColumns.tmst)
    } catch {
      case _: Throwable => defTmst
    }
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

  private def getFilterTableDataFrame(context: DQContext): Option[DataFrame] =
    filterTableNameOpt.flatMap(getDataFrame(context, _))

  private def getBatchRecords(context: DQContext): Option[DataFrame] = {
    getDataFrame(context, inputName)
  }

  private def getStreamingRecords(
      context: DQContext): (Option[RDD[(Long, Iterable[String])]], Set[Long]) = {
    implicit val encoder: Encoder[(Long, String)] =
      Encoders.tuple(Encoders.scalaLong, Encoders.STRING)
    val defTimestamp = context.contextId.timestamp
    getDataFrame(context, inputName) match {
      case Some(df) =>
        val (filterFuncOpt, emptyTimestamps) = getFilterTableDataFrame(context) match {
          case Some(filterDf) =>
            // timestamps with empty flag
            val tmsts: Array[(Long, Boolean)] = filterDf.collect.flatMap { row =>
              try {
                val tmst = getTmst(row, defTimestamp)
                val empty = row.getAs[Boolean](ConstantColumns.empty)
                Some((tmst, empty))
              } catch {
                case _: Throwable => None
              }
            }
            val emptyTmsts = tmsts.filter(_._2).map(_._1).toSet
            val recordTmsts = tmsts.filter(!_._2).map(_._1).toSet
            val filterFuncOpt: Option[Long => Boolean] = if (recordTmsts.nonEmpty) {
              Some((t: Long) => recordTmsts.contains(t))
            } else None

            (filterFuncOpt, emptyTmsts)
          case _ => (Some((_: Long) => true), Set[Long]())
        }

        // filter timestamps need to record
        filterFuncOpt match {
          case Some(filterFunc) =>
            val records = df.flatMap { row =>
              val tmst = getTmst(row, defTimestamp)
              if (filterFunc(tmst)) {
                try {
                  val map = SparkRowFormatter.formatRow(row)
                  val str = JsonUtil.toJson(map)
                  Some((tmst, str))
                } catch {
                  case _: Throwable => None
                }
              } else None
            }
            (Some(records.rdd.groupByKey), emptyTimestamps)
          case _ => (None, emptyTimestamps)
        }
      case _ => (None, Set[Long]())
    }
  }

}
