
package com.stackstech.honeybee.bees.step.builder.preproc

import com.stackstech.honeybee.bees.configuration.dqdefinition.RuleParam
import com.stackstech.honeybee.bees.configuration.enums.DslType._

/**
 * generate each entity pre-proc params by template defined in pre-proc param
 */
object PreProcParamMaker {

  case class StringAnyMap(values: Map[String, Any])

  def makePreProcRules(
      rules: Seq[RuleParam],
      suffix: String,
      dfName: String): (Seq[RuleParam], String) = {
    val len = rules.size
    val (newRules, _) = rules.zipWithIndex.foldLeft((Nil: Seq[RuleParam], dfName)) {
      (ret, pair) =>
        val (rls, prevOutDfName) = ret
        val (rule, i) = pair
        val inName = rule.getInDfName(prevOutDfName)
        val outName = if (i == len - 1) dfName else rule.getOutDfName(genNameWithIndex(dfName, i))
        val ruleWithNames = rule.replaceInOutDfName(inName, outName)
        (rls :+ makeNewPreProcRule(ruleWithNames, suffix), outName)
    }
    (newRules, withSuffix(dfName, suffix))
  }

  private def makeNewPreProcRule(rule: RuleParam, suffix: String): RuleParam = {
    val newInDfName = withSuffix(rule.getInDfName(), suffix)
    val newOutDfName = withSuffix(rule.getOutDfName(), suffix)
    val rpRule = rule.replaceInOutDfName(newInDfName, newOutDfName)
    rule.getDslType match {
      case DataFrameOpsType => rpRule
      case _ =>
        val newRule = replaceDfNameSuffix(rule.getRule, rule.getInDfName(), suffix)
        rpRule.replaceRule(newRule)
    }
  }

  private def genNameWithIndex(name: String, i: Int): String = s"$name$i"

  private def replaceDfNameSuffix(str: String, dfName: String, suffix: String): String = {
    val regexStr = s"(?i)$dfName"
    val replaceDfName = withSuffix(dfName, suffix)
    str.replaceAll(regexStr, replaceDfName)
  }

  def withSuffix(str: String, suffix: String): String = s"${str}_$suffix"

}
