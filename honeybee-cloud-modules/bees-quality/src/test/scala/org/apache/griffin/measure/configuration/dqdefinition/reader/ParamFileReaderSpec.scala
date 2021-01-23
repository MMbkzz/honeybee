
package com.stackstech.honeybee.bees.configuration.dqdefinition.reader

import org.scalatest._
import scala.util.{Failure, Success}

import com.stackstech.honeybee.bees.configuration.dqdefinition.DQConfig
import com.stackstech.honeybee.bees.configuration.enums.DslType.GriffinDsl

class ParamFileReaderSpec extends FlatSpec with Matchers {

  "params " should "be parsed from a valid file" in {
    val reader: ParamReader =
      ParamFileReader(getClass.getResource("/_accuracy-batch-griffindsl.json").getFile)
    val params = reader.readConfig[DQConfig]
    params match {
      case Success(v) =>
        v.getEvaluateRule.getRules.head.getDslType should ===(GriffinDsl)
        v.getEvaluateRule.getRules.head.getOutDfName() should ===("accu")
      case Failure(_) =>
        fail("it should not happen")
    }

  }

  it should "fail for an invalid file" in {
    val reader: ParamReader = ParamFileReader(
      getClass.getResource("/invalidconfigs/missingrule_accuracy_batch_sparksql.json").getFile)
    val params = reader.readConfig[DQConfig]
    params match {
      case Success(_) =>
        fail("it is an invalid config file")
      case Failure(e) =>
        e.getMessage contains "evaluate.rule should not be null"
    }

  }

  it should "fail for an invalid completeness json file" in {
    val reader: ParamFileReader = ParamFileReader(
      getClass
        .getResource("/invalidconfigs/invalidtype_completeness_batch_griffindal.json")
        .getFile)
    val params = reader.readConfig[DQConfig]
    params match {
      case Success(_) =>
        fail("it is an invalid config file")
      case Failure(e) =>
        e.getMessage contains "error error.conf type"
    }
  }

  it should "be parsed from a valid errorconf completeness json file" in {
    val reader: ParamReader = ParamFileReader(
      getClass.getResource("/_completeness_errorconf-batch-griffindsl.json").getFile)
    val params = reader.readConfig[DQConfig]
    params match {
      case Success(v) =>
        v.getEvaluateRule.getRules.head.getErrorConfs.length should ===(2)
        v.getEvaluateRule.getRules.head.getErrorConfs.head.getColumnName.get should ===("user")
        v.getEvaluateRule.getRules.head.getErrorConfs(1).getColumnName.get should ===("name")
      case Failure(_) =>
        fail("it should not happen")
    }
  }
}
