
package com.stackstech.honeybee.bees.configuration.dqdefinition.reader

import scala.io.Source

import org.scalatest.{FlatSpec, Matchers}
import scala.util.{Failure, Success}

import com.stackstech.honeybee.bees.configuration.dqdefinition.DQConfig
import com.stackstech.honeybee.bees.configuration.enums.DslType.GriffinDsl

class ParamJsonReaderSpec extends FlatSpec with Matchers {

  "params " should "be parsed from a valid file" in {
    val bufferedSource =
      Source.fromFile(getClass.getResource("/_accuracy-batch-griffindsl.json").getFile)
    val jsonString = bufferedSource.getLines().mkString
    bufferedSource.close

    val reader: ParamReader = ParamJsonReader(jsonString)
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
    val bufferedSource = Source.fromFile(
      getClass.getResource("/invalidconfigs/missingrule_accuracy_batch_sparksql.json").getFile)
    val jsonString = bufferedSource.getLines().mkString
    bufferedSource.close

    val reader: ParamReader = ParamJsonReader(jsonString)
    val params = reader.readConfig[DQConfig]
    params match {
      case Success(_) =>
        fail("it is an invalid config file")
      case Failure(e) =>
        e.getMessage should include("evaluate.rule should not be null")
    }

  }

}
