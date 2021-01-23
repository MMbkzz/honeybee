
package com.stackstech.honeybee.bees.step.builder

import com.stackstech.honeybee.bees.configuration.dqdefinition.RuleParam
import com.stackstech.honeybee.bees.context.DQContext
import com.stackstech.honeybee.bees.step.DQStep
import com.stackstech.honeybee.bees.step.builder.dsl.parser.GriffinDslParser
import com.stackstech.honeybee.bees.step.builder.dsl.transform.Expr2DQSteps

case class GriffinDslDQStepBuilder(dataSourceNames: Seq[String], functionNames: Seq[String])
    extends RuleParamStepBuilder {

  val filteredFunctionNames: Seq[String] = functionNames.filter { fn =>
    fn.matches("""^[a-zA-Z_]\w*$""")
  }
  val parser: GriffinDslParser = GriffinDslParser(dataSourceNames, filteredFunctionNames)

  def buildSteps(context: DQContext, ruleParam: RuleParam): Seq[DQStep] = {
    val name = getStepName(ruleParam.getOutDfName())
    val rule = ruleParam.getRule
    val dqType = ruleParam.getDqType
    try {
      val result = parser.parseRule(rule, dqType)
      if (result.successful) {
        val expr = result.get
        val expr2DQSteps = Expr2DQSteps(context, expr, ruleParam.replaceOutDfName(name))
        expr2DQSteps.getDQSteps
      } else {
        warn(s"parse rule [ $rule ] fails: \n$result")
        Nil
      }
    } catch {
      case e: Throwable =>
        error(s"generate rule plan $name fails: ${e.getMessage}", e)
        Nil
    }
  }

}
