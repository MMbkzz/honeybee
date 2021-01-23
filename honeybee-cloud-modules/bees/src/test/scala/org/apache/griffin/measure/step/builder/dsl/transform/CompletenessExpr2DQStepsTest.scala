
package com.stackstech.honeybee.bees.step.builder.dsl.transform

import org.scalatest._
import org.scalatest.mockito.MockitoSugar

import com.stackstech.honeybee.bees.configuration.dqdefinition.RuleErrorConfParam
import com.stackstech.honeybee.bees.configuration.dqdefinition.RuleParam
import com.stackstech.honeybee.bees.context.DQContext
import com.stackstech.honeybee.bees.step.builder.dsl.expr.Expr

class CompletenessExpr2DQStepsTest extends FlatSpec with Matchers with MockitoSugar {

  "CompletenessExpr2DQSteps" should "get correct where clause" in {
    val completeness = CompletenessExpr2DQSteps(mock[DQContext], mock[Expr], mock[RuleParam])

    val regexClause =
      completeness.getEachErrorWhereClause(RuleErrorConfParam("id", "regex", List(raw"\d+")))
    regexClause shouldBe raw"(`id` REGEXP '\\d+')"

    val enumerationClause = completeness.getEachErrorWhereClause(
      RuleErrorConfParam("id", "enumeration", List("1", "2", "3")))
    enumerationClause shouldBe "(`id` IN ('1', '2', '3'))"

    val noneClause = completeness.getEachErrorWhereClause(
      RuleErrorConfParam("id", "enumeration", List("hive_none")))
    noneClause shouldBe "(`id` IS NULL)"

    val fullClause = completeness.getEachErrorWhereClause(
      RuleErrorConfParam("id", "enumeration", List("1", "hive_none", "3", "foo,bar")))
    fullClause shouldBe "(`id` IN ('1', '3', 'foo,bar') OR `id` IS NULL)"
  }
}
