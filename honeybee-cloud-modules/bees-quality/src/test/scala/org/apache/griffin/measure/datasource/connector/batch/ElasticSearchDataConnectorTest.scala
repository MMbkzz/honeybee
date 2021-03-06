
package com.stackstech.honeybee.bees.datasource.connector.batch

import org.apache.spark.sql.types.StructType
import org.scalatest.Matchers
import org.testcontainers.elasticsearch.ElasticsearchContainer

import com.stackstech.honeybee.bees.SparkSuiteBase
import com.stackstech.honeybee.bees.configuration.dqdefinition.DataConnectorParam
import com.stackstech.honeybee.bees.datasource.TimestampStorage

class ElasticSearchDataConnectorTest extends SparkSuiteBase with Matchers {

  // ignorance flag that could skip cases
  private var ignoreCase: Boolean = false

  //  private var client: RestClient = _
  private var container: ElasticsearchContainer = _
  private var ES_HTTP_PORT: Int = _

  private final val INDEX1 = "bank"
  private final val INDEX2 = "car"

  private final val timestampStorage = TimestampStorage()

  private final val dcParam =
    DataConnectorParam(
      conType = "es_dcp",
      dataFrameName = "test_df",
      config = Map.empty,
      preProc = Nil)

  private def createIndexWithData(index: String, path: String): Unit = {
    val filePath = ClassLoader.getSystemResource(path).getPath

    val command =
      s"""curl -X POST localhost:$ES_HTTP_PORT/$index/_bulk?pretty&refresh
         |-H Content-type:application/json
         |--data-binary @$filePath""".stripMargin

    println(s"Executing Command: '$command'")
    scala.sys.process.stringToProcess(command).!!
  }

  override def beforeAll(): Unit = {
    super.beforeAll()
    try {
      container = new ElasticsearchContainer(
        "docker.elastic.co/elasticsearch/elasticsearch-oss:6.4.1")
      container.start()
      ES_HTTP_PORT = container.getHttpHostAddress.split(":").last.toInt
      createIndexWithData(INDEX1, "elasticsearch/test_data_1.json")
      createIndexWithData(INDEX2, "elasticsearch/test_data_2.json")
    } catch {
      case _: Throwable =>
        ignoreCase = true
        None
    }
  }

  override def afterAll(): Unit = {
    super.afterAll()
    if (!ignoreCase) {
      container.close()
    }
  }

  "elastic search data connector" should "be able to read from embedded server" in {
    if (!ignoreCase) {
      val configs = Map(
        "paths" -> Seq(INDEX1),
        "options" -> Map("es.nodes" -> "localhost", "es.port" -> ES_HTTP_PORT))
      val dc = ElasticSearchDataConnector(spark, dcParam.copy(config = configs), timestampStorage)
      val result = dc.data(1000L)

      assert(result._1.isDefined)
      assert(result._1.get.collect().length == 1000)
    }
  }

  it should "be able to read from multiple indices and merge their schemas" in {
    if (!ignoreCase) {
      val configs = Map(
        "paths" -> Seq(INDEX1, INDEX2),
        "options" -> Map("es.nodes" -> "localhost", "es.port" -> ES_HTTP_PORT))
      val dc = ElasticSearchDataConnector(spark, dcParam.copy(config = configs), timestampStorage)
      val result = dc.data(1000L)

      assert(result._1.isDefined)
      assert(result._1.get.collect().length == 1002)

      val expectedSchema = new StructType()
        .add("description", "string")
        .add("manufacturer", "string")
        .add("model", "string")
        .add("account_number", "bigint")
        .add("address", "string")
        .add("age", "bigint")
        .add("balance", "bigint")
        .add("city", "string")
        .add("email", "string")
        .add("employer", "string")
        .add("firstname", "string")
        .add("gender", "string")
        .add("lastname", "string")
        .add("state", "string")
        .add("__tmst", "bigint", nullable = false)

      result._1.get.schema.fields should contain theSameElementsAs expectedSchema.fields
    }
  }

  it should "respect selection expression" in {
    if (!ignoreCase) {
      val configs = Map(
        "paths" -> Seq(INDEX1, INDEX2),
        "options" -> Map("es.nodes" -> "localhost", "es.port" -> ES_HTTP_PORT),
        "selectionExprs" -> Seq("account_number", "age > 10 as is_adult"))
      val dc = ElasticSearchDataConnector(spark, dcParam.copy(config = configs), timestampStorage)
      val result = dc.data(1000L)

      assert(result._1.isDefined)
      assert(result._1.get.collect().length == 1002)

      val expectedSchema = new StructType()
        .add("account_number", "bigint")
        .add("is_adult", "boolean")
        .add("__tmst", "bigint", nullable = false)

      result._1.get.schema.fields should contain theSameElementsAs expectedSchema.fields
    }
  }

  it should "respect filter conditions" in {
    if (!ignoreCase) {
      val configs = Map(
        "paths" -> Seq(INDEX1, INDEX2),
        "options" -> Map("es.nodes" -> "localhost", "es.port" -> ES_HTTP_PORT),
        "selectionExprs" -> Seq("account_number"),
        "filterExprs" -> Seq("account_number < 10"))
      val dc = ElasticSearchDataConnector(spark, dcParam.copy(config = configs), timestampStorage)
      val result = dc.data(1000L)

      assert(result._1.isDefined)
      assert(result._1.get.collect().length == 10)

      val expectedSchema = new StructType()
        .add("account_number", "bigint")
        .add("__tmst", "bigint", nullable = false)

      result._1.get.schema.fields should contain theSameElementsAs expectedSchema.fields
    }
  }

}
