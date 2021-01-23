
package com.stackstech.honeybee.bees.datasource.connector

import java.util.concurrent.atomic.AtomicLong

import scala.collection.mutable

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._

import com.stackstech.honeybee.bees.Loggable
import com.stackstech.honeybee.bees.configuration.dqdefinition.DataConnectorParam
import com.stackstech.honeybee.bees.configuration.enums.ProcessType.BatchProcessType
import com.stackstech.honeybee.bees.context.{ContextId, DQContext, TimeRange}
import com.stackstech.honeybee.bees.datasource.TimestampStorage
import com.stackstech.honeybee.bees.job.builder.DQJobBuilder
import com.stackstech.honeybee.bees.step.builder.ConstantColumns
import com.stackstech.honeybee.bees.step.builder.preproc.PreProcParamMaker

trait DataConnector extends Loggable with Serializable {

  val sparkSession: SparkSession

  val dcParam: DataConnectorParam

  val id: String = DataConnectorIdGenerator.genId

  val timestampStorage: TimestampStorage
  protected def saveTmst(t: Long): mutable.SortedSet[Long] = timestampStorage.insert(t)
  protected def readTmst(t: Long): Set[Long] = timestampStorage.fromUntil(t, t + 1)

  def init(): Unit

  // get data frame in batch mode
  def data(ms: Long): (Option[DataFrame], TimeRange)

  private def createContext(t: Long): DQContext = {
    DQContext(ContextId(t, id), id, Nil, Nil, BatchProcessType)(sparkSession)
  }

  def preProcess(dfOpt: Option[DataFrame], ms: Long): Option[DataFrame] = {
    // new context
    val context = createContext(ms)

    val timestamp = context.contextId.timestamp
    val suffix = context.contextId.id
    val dcDfName = dcParam.getDataFrameName("this")

    try {
      saveTmst(timestamp) // save timestamp

      dfOpt.flatMap { df =>
        val (preProcRules, thisTable) =
          PreProcParamMaker.makePreProcRules(dcParam.getPreProcRules, suffix, dcDfName)

        // init data
        context.compileTableRegister.registerTable(thisTable)
        context.runTimeTableRegister.registerTable(thisTable, df)

        // build job
        val preprocJob = DQJobBuilder.buildDQJob(context, preProcRules)

        // job execute
        preprocJob.execute(context)

        // out data
        val outDf = context.sparkSession.table(s"`$thisTable`")

        // add tmst column
        val withTmstDf = outDf.withColumn(ConstantColumns.tmst, lit(timestamp))

        // clean context
        context.clean()

        Some(withTmstDf)
      }

    } catch {
      case e: Throwable =>
        error(s"pre-process of data connector [$id] error: ${e.getMessage}", e)
        None
    }
  }
}

object DataConnectorIdGenerator {
  private val counter: AtomicLong = new AtomicLong(0L)
  private val head: String = "dc"

  def genId: String = {
    s"$head$increment"
  }

  private def increment: Long = {
    counter.incrementAndGet()
  }
}
