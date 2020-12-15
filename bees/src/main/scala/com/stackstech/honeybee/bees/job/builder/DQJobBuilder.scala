
package com.stackstech.honeybee.bees.job.builder

import com.stackstech.honeybee.bees.configuration.dqdefinition._
import com.stackstech.honeybee.bees.context.DQContext
import com.stackstech.honeybee.bees.job._
import com.stackstech.honeybee.bees.step.builder.DQStepBuilder
import com.stackstech.honeybee.bees.step.write.MetricFlushStep

/**
 * build dq job based on configuration
 */
object DQJobBuilder {

  /**
   * build dq job with rule param
   * @param context              dq context
   * @param evaluateRuleParam    evaluate rule param
   * @return       dq job
   */
  def buildDQJob(context: DQContext, evaluateRuleParam: EvaluateRuleParam): DQJob = {
    val ruleParams = evaluateRuleParam.getRules
    buildDQJob(context, ruleParams)
  }

  /**
   * build dq job with rules in evaluate rule param or pre-proc param
   * @param context          dq context
   * @param ruleParams       rule params
   * @return       dq job
   */
  def buildDQJob(context: DQContext, ruleParams: Seq[RuleParam]): DQJob = {
    // build steps by datasources
    val dsSteps = context.dataSources.flatMap { dataSource =>
      DQStepBuilder.buildStepOptByDataSourceParam(context, dataSource.dsParam)
    }
    // build steps by rules
    val ruleSteps = ruleParams.flatMap { ruleParam =>
      DQStepBuilder.buildStepOptByRuleParam(context, ruleParam)
    }
    // metric flush step
    val metricFlushStep = MetricFlushStep()

    DQJob(dsSteps ++ ruleSteps :+ metricFlushStep)
  }

}
