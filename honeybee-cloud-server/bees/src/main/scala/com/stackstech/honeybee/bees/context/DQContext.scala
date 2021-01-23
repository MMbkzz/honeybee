
package com.stackstech.honeybee.bees.context

import org.apache.spark.sql.{Encoder, Encoders, SparkSession}

import com.stackstech.honeybee.bees.configuration.dqdefinition._
import com.stackstech.honeybee.bees.configuration.enums.ProcessType._
import com.stackstech.honeybee.bees.configuration.enums.WriteMode
import com.stackstech.honeybee.bees.datasource._
import com.stackstech.honeybee.bees.sink.{Sink, SinkFactory}

/**
 * dq context: the context of each calculation
 * unique context id in each calculation
 * access the same spark session this app created
 */
case class DQContext(
    contextId: ContextId,
    name: String,
    dataSources: Seq[DataSource],
    sinkParams: Seq[SinkParam],
    procType: ProcessType)(@transient implicit val sparkSession: SparkSession) {

  val compileTableRegister: CompileTableRegister = CompileTableRegister()
  val runTimeTableRegister: RunTimeTableRegister = RunTimeTableRegister(sparkSession)

  val dataFrameCache: DataFrameCache = DataFrameCache()

  val metricWrapper: MetricWrapper = MetricWrapper(name, sparkSession.sparkContext.applicationId)
  val writeMode: WriteMode = WriteMode.defaultMode(procType)

  val dataSourceNames: Seq[String] = {
    // sort data source names, put baseline data source name to the head
    val (blOpt, others) = dataSources.foldLeft((None: Option[String], Nil: Seq[String])) {
      (ret, ds) =>
        val (opt, seq) = ret
        if (opt.isEmpty && ds.isBaseline) (Some(ds.name), seq) else (opt, seq :+ ds.name)
    }
    blOpt match {
      case Some(bl) => bl +: others
      case _ => others
    }
  }
  dataSourceNames.foreach(name => compileTableRegister.registerTable(name))

  def getDataSourceName(index: Int): String = {
    if (dataSourceNames.size > index) dataSourceNames(index) else ""
  }

  implicit val encoder: Encoder[String] = Encoders.STRING
  val functionNames: Seq[String] = sparkSession.catalog.listFunctions.map(_.name).collect.toSeq

  val dataSourceTimeRanges: Map[String, TimeRange] = loadDataSources()

  def loadDataSources(): Map[String, TimeRange] = {
    dataSources.map { ds =>
      (ds.name, ds.loadData(this))
    }.toMap
  }

  printTimeRanges()

  private val sinkFactory = SinkFactory(sinkParams, name)
  private val defaultSinks: Seq[Sink] = createSinks(contextId.timestamp)

  def getSinks(timestamp: Long): Seq[Sink] = {
    if (timestamp == contextId.timestamp) getSinks
    else createSinks(timestamp)
  }

  def getSinks: Seq[Sink] = defaultSinks

  private def createSinks(t: Long): Seq[Sink] = {
    procType match {
      case BatchProcessType => sinkFactory.getSinks(t, block = true)
      case StreamingProcessType => sinkFactory.getSinks(t, block = false)
    }
  }

  def cloneDQContext(newContextId: ContextId): DQContext = {
    DQContext(newContextId, name, dataSources, sinkParams, procType)(sparkSession)
  }

  def clean(): Unit = {
    compileTableRegister.unregisterAllTables()
    runTimeTableRegister.unregisterAllTables()

    dataFrameCache.uncacheAllDataFrames()
    dataFrameCache.clearAllTrashDataFrames()
  }

  private def printTimeRanges(): Unit = {
    if (dataSourceTimeRanges.nonEmpty) {
      val timeRangesStr = dataSourceTimeRanges
        .map { pair =>
          val (name, timeRange) = pair
          s"$name -> (${timeRange.begin}, ${timeRange.end}]"
        }
        .mkString(", ")
      println(s"data source timeRanges: $timeRangesStr")
    }
  }

}
