
package com.stackstech.honeybee.bees.configuration.dqdefinition.reader

import org.scalatest.{FlatSpec, Matchers}

import com.stackstech.honeybee.bees.configuration.dqdefinition.{
  DQConfig,
  EvaluateRuleParam,
  RuleOutputParam,
  RuleParam
}

class ParamEnumReaderSpec extends FlatSpec with Matchers {
  import com.stackstech.honeybee.bees.configuration.enums.DslType._
  "dsltype" should "be parsed to predefined set of values" in {
    val validDslSparkSqlValues =
      Seq("spark-sql", "spark-SQL", "SPARK-SQL", "sparksql")
    validDslSparkSqlValues foreach { x =>
      val ruleParam = RuleParam(x, "accuracy")
      ruleParam.getDslType should ===(SparkSql)
    }
    val invalidDslSparkSqlValues = Seq("spark", "sql", "")
    invalidDslSparkSqlValues foreach { x =>
      val ruleParam = RuleParam(x, "accuracy")
      ruleParam.getDslType should not be SparkSql
    }

    val validDslGriffinValues =
      Seq("griffin-dsl", "griffindsl", "griFfin-dsl", "")
    validDslGriffinValues foreach { x =>
      val ruleParam = RuleParam(x, "accuracy")
      ruleParam.getDslType should ===(GriffinDsl)
    }

    val validDslDfOpsValues =
      Seq("df-ops", "dfops", "DFOPS", "df-opr", "dfopr", "df-operations", "dfoperations")
    validDslDfOpsValues foreach { x =>
      val ruleParam = RuleParam(x, "accuracy")
      ruleParam.getDslType should ===(DataFrameOpsType)
    }

    val invalidDslDfOpsValues = Seq("df-oprts", "-")
    invalidDslDfOpsValues foreach { x =>
      val ruleParam = RuleParam(x, "accuracy")
      ruleParam.getDslType should not be DataFrameOpsType
    }
  }

  "griffindsl" should "be returned as default dsl type" in {
    import com.stackstech.honeybee.bees.configuration.enums.DslType._
    val dslGriffinDslValues = Seq("griffin", "dsl")
    dslGriffinDslValues foreach { x =>
      val ruleParam = RuleParam(x, "accuracy")
      ruleParam.getDslType should be(GriffinDsl)
    }
  }

  "dqtype" should "be parsed to predefined set of values" in {
    import com.stackstech.honeybee.bees.configuration.enums.DqType._
    var ruleParam = RuleParam("griffin-dsl", "accuracy")
    ruleParam.getDqType should be(Accuracy)
    ruleParam = RuleParam("griffin-dsl", "accu")
    ruleParam.getDqType should not be Accuracy
    ruleParam.getDqType should be(Unknown)

    ruleParam = RuleParam("griffin-dsl", "profiling")
    ruleParam.getDqType should be(Profiling)
    ruleParam = RuleParam("griffin-dsl", "profilin")
    ruleParam.getDqType should not be Profiling
    ruleParam.getDqType should be(Unknown)

    ruleParam = RuleParam("griffin-dsl", "TIMELINESS")
    ruleParam.getDqType should be(Timeliness)
    ruleParam = RuleParam("griffin-dsl", "timeliness ")
    ruleParam.getDqType should not be Timeliness
    ruleParam.getDqType should be(Unknown)

    ruleParam = RuleParam("griffin-dsl", "UNIQUENESS")
    ruleParam.getDqType should be(Uniqueness)
    ruleParam = RuleParam("griffin-dsl", "UNIQUE")
    ruleParam.getDqType should not be Uniqueness
    ruleParam.getDqType should be(Unknown)

    ruleParam = RuleParam("griffin-dsl", "Duplicate")
    ruleParam.getDqType should be(Uniqueness)
    ruleParam = RuleParam("griffin-dsl", "duplica")
    ruleParam.getDqType should not be Duplicate
    ruleParam.getDqType should be(Unknown)

    ruleParam = RuleParam("griffin-dsl", "COMPLETENESS")
    ruleParam.getDqType should be(Completeness)
    ruleParam = RuleParam("griffin-dsl", "complete")
    ruleParam.getDqType should not be Completeness
    ruleParam.getDqType should be(Unknown)

    ruleParam = RuleParam("griffin-dsl", "")
    ruleParam.getDqType should be(Unknown)
    ruleParam = RuleParam("griffin-dsl", "duplicate")
    ruleParam.getDqType should not be Unknown
  }

  "outputtype" should "be valid" in {
    import com.stackstech.honeybee.bees.configuration.enums.OutputType._
    var ruleOutputParam = RuleOutputParam("metric", "", "map")
    ruleOutputParam.getOutputType should be(MetricOutputType)
    ruleOutputParam = RuleOutputParam("metr", "", "map")
    ruleOutputParam.getOutputType should not be MetricOutputType
    ruleOutputParam.getOutputType should be(UnknownOutputType)

    ruleOutputParam = RuleOutputParam("record", "", "map")
    ruleOutputParam.getOutputType should be(RecordOutputType)
    ruleOutputParam = RuleOutputParam("rec", "", "map")
    ruleOutputParam.getOutputType should not be RecordOutputType
    ruleOutputParam.getOutputType should be(UnknownOutputType)

    ruleOutputParam = RuleOutputParam("dscupdate", "", "map")
    ruleOutputParam.getOutputType should be(DscUpdateOutputType)
    ruleOutputParam = RuleOutputParam("dsc", "", "map")
    ruleOutputParam.getOutputType should not be DscUpdateOutputType
    ruleOutputParam.getOutputType should be(UnknownOutputType)

  }

  "flattentype" should "be valid" in {
    import com.stackstech.honeybee.bees.configuration.enums.FlattenType._
    var ruleOutputParam = RuleOutputParam("metric", "", "map")
    ruleOutputParam.getFlatten should be(MapFlattenType)
    ruleOutputParam = RuleOutputParam("metric", "", "metr")
    ruleOutputParam.getFlatten should not be MapFlattenType
    ruleOutputParam.getFlatten should be(DefaultFlattenType)

    ruleOutputParam = RuleOutputParam("metric", "", "array")
    ruleOutputParam.getFlatten should be(ArrayFlattenType)
    ruleOutputParam = RuleOutputParam("metric", "", "list")
    ruleOutputParam.getFlatten should be(ArrayFlattenType)
    ruleOutputParam = RuleOutputParam("metric", "", "arrays")
    ruleOutputParam.getFlatten should not be ArrayFlattenType
    ruleOutputParam.getFlatten should be(DefaultFlattenType)

    ruleOutputParam = RuleOutputParam("metric", "", "entries")
    ruleOutputParam.getFlatten should be(EntriesFlattenType)
    ruleOutputParam = RuleOutputParam("metric", "", "entry")
    ruleOutputParam.getFlatten should not be EntriesFlattenType
    ruleOutputParam.getFlatten should be(DefaultFlattenType)
  }

  "sinktype" should "be valid" in {
    import org.mockito.Mockito._

    import com.stackstech.honeybee.bees.configuration.enums.SinkType._
    var dqConfig = DQConfig(
      "test",
      1234,
      "",
      Nil,
      mock(classOf[EvaluateRuleParam]),
      List(
        "Console",
        "Log",
        "CONSOLE",
        "LOG",
        "Es",
        "ElasticSearch",
        "Http",
        "MongoDB",
        "mongo",
        "hdfs"))
    dqConfig.getValidSinkTypes should be(Seq(Console, ElasticSearch, MongoDB, Hdfs))
    dqConfig =
      DQConfig("test", 1234, "", Nil, mock(classOf[EvaluateRuleParam]), List("Consol", "Logg"))
    dqConfig.getValidSinkTypes should not be Seq(Console)
    dqConfig.getValidSinkTypes should be(Seq())

    dqConfig = DQConfig("test", 1234, "", Nil, mock(classOf[EvaluateRuleParam]), List(""))
    dqConfig.getValidSinkTypes should be(Nil)
  }

}
