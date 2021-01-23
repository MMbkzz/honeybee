
package com.stackstech.honeybee.bees.step.builder

import com.stackstech.honeybee.bees.configuration.dqdefinition.RuleParam
import com.stackstech.honeybee.bees.context.DQContext
import com.stackstech.honeybee.bees.step.DQStep
import com.stackstech.honeybee.bees.step.transform.SparkSqlTransformStep

case class SparkSqlDQStepBuilder() extends RuleParamStepBuilder {

  def buildSteps(context: DQContext, ruleParam: RuleParam): Seq[DQStep] = {
    val name = getStepName(ruleParam.getOutDfName())
    val transformStep = SparkSqlTransformStep(
      name,
      ruleParam.getRule,
      ruleParam.getDetails,
      None,
      ruleParam.getCache)
    transformStep +: buildDirectWriteSteps(ruleParam)
  }

}
