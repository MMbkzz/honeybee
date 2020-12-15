
package com.stackstech.honeybee.bees.transformations

import org.apache.spark.sql.DataFrame
import org.scalatest._

import com.stackstech.honeybee.bees.SparkSuiteBase
import com.stackstech.honeybee.bees.configuration.dqdefinition._
import com.stackstech.honeybee.bees.configuration.enums.ProcessType.BatchProcessType
import com.stackstech.honeybee.bees.context.{ContextId, DQContext}
import com.stackstech.honeybee.bees.datasource.DataSourceFactory
import com.stackstech.honeybee.bees.job.builder.DQJobBuilder

case class AccuracyResult(total: Long, miss: Long, matched: Long, matchedFraction: Double)

class AccuracyTransformationsIntegrationTest extends FlatSpec with Matchers with SparkSuiteBase {
  private val EMPTY_PERSON_TABLE = "empty_person"
  private val PERSON_TABLE = "person"

  override def beforeAll(): Unit = {
    super.beforeAll()

    dropTables()
    createPersonTable()
    createEmptyPersonTable()

    spark.conf.set("spark.sql.crossJoin.enabled", "true")
  }

  override def afterAll(): Unit = {
    dropTables()
    super.afterAll()
  }

  "accuracy" should "basically work" in {
    checkAccuracy(
      sourceName = PERSON_TABLE,
      targetName = PERSON_TABLE,
      expectedResult = AccuracyResult(total = 2, miss = 0, matched = 2, matchedFraction = 1.0))
  }

  "accuracy" should "work with empty target" in {
    checkAccuracy(
      sourceName = PERSON_TABLE,
      targetName = EMPTY_PERSON_TABLE,
      expectedResult = AccuracyResult(total = 2, miss = 2, matched = 0, matchedFraction = 0.0))
  }

  "accuracy" should "work with empty source" in {
    checkAccuracy(
      sourceName = EMPTY_PERSON_TABLE,
      targetName = PERSON_TABLE,
      expectedResult = AccuracyResult(total = 0, miss = 0, matched = 0, matchedFraction = 1.0))
  }

  "accuracy" should "work with empty source and target" in {
    checkAccuracy(
      sourceName = EMPTY_PERSON_TABLE,
      targetName = EMPTY_PERSON_TABLE,
      expectedResult = AccuracyResult(total = 0, miss = 0, matched = 0, matchedFraction = 1.0))
  }

  private def checkAccuracy(
      sourceName: String,
      targetName: String,
      expectedResult: AccuracyResult) = {
    val dqContext: DQContext = getDqContext(
      dataSourcesParam = List(
        DataSourceParam(name = "source", connector = dataConnectorParam(tableName = sourceName)),
        DataSourceParam(name = "target", connector = dataConnectorParam(tableName = targetName))))

    val accuracyRule = RuleParam(
      dslType = "griffin-dsl",
      dqType = "ACCURACY",
      outDfName = "person_accuracy",
      rule = "source.name = target.name")

    val spark = this.spark
    import spark.implicits._
    val res = getRuleResults(dqContext, accuracyRule)
      .as[AccuracyResult]
      .collect()

    res.length shouldBe 1

    res(0) shouldEqual expectedResult
  }

  private def getRuleResults(dqContext: DQContext, rule: RuleParam): DataFrame = {
    val dqJob =
      DQJobBuilder.buildDQJob(dqContext, evaluateRuleParam = EvaluateRuleParam(List(rule)))

    dqJob.execute(dqContext)

    spark.sql(s"select * from ${rule.getOutDfName()}")
  }

  private def createPersonTable(): Unit = {
    val personCsvPath = getClass.getResource("/hive/person_table.csv").getFile

    spark.sql(
      s"CREATE TABLE $PERSON_TABLE " +
        "( " +
        "  name String," +
        "  age int " +
        ") " +
        "ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' " +
        "STORED AS TEXTFILE")

    spark.sql(s"LOAD DATA LOCAL INPATH '$personCsvPath' OVERWRITE INTO TABLE $PERSON_TABLE")
  }

  private def createEmptyPersonTable(): Unit = {
    spark.sql(
      s"CREATE TABLE $EMPTY_PERSON_TABLE " +
        "( " +
        "  name String," +
        "  age int " +
        ") " +
        "ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' " +
        "STORED AS TEXTFILE")

    spark.sql(s"select * from $EMPTY_PERSON_TABLE").show()
  }

  private def dropTables(): Unit = {
    spark.sql(s"DROP TABLE IF EXISTS $PERSON_TABLE ")
    spark.sql(s"DROP TABLE IF EXISTS $EMPTY_PERSON_TABLE ")
  }

  private def getDqContext(
      dataSourcesParam: Seq[DataSourceParam],
      name: String = "test-context"): DQContext = {
    val dataSources = DataSourceFactory.getDataSources(spark, null, dataSourcesParam)
    dataSources.foreach(_.init())

    DQContext(ContextId(System.currentTimeMillis), name, dataSources, Nil, BatchProcessType)(
      spark)
  }

  private def dataConnectorParam(tableName: String) = {
    DataConnectorParam(
      conType = "HIVE",
      dataFrameName = null,
      config = Map("table.name" -> tableName),
      preProc = null)
  }
}
