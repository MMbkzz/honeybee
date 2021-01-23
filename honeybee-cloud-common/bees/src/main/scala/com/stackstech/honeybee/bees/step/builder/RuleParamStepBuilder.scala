
package com.stackstech.honeybee.bees.step.builder

import com.stackstech.honeybee.bees.configuration.dqdefinition.RuleParam
import com.stackstech.honeybee.bees.configuration.enums.OutputType._
import com.stackstech.honeybee.bees.context.DQContext
import com.stackstech.honeybee.bees.step.{DQStep, SeqDQStep}
import com.stackstech.honeybee.bees.step.write.{
  DataSourceUpdateWriteStep,
  MetricWriteStep,
  RecordWriteStep
}

/**
 * build dq step by rule param
 */
trait RuleParamStepBuilder extends DQStepBuilder {

  type ParamType = RuleParam

  def buildDQStep(context: DQContext, param: ParamType): Option[DQStep] = {
    val steps = buildSteps(context, param)
    if (steps.size > 1) Some(SeqDQStep(steps))
    else if (steps.size == 1) steps.headOption
    else None
  }

  def buildSteps(context: DQContext, ruleParam: RuleParam): Seq[DQStep]

  protected def buildDirectWriteSteps(ruleParam: RuleParam): Seq[DQStep] = {
    val name = getStepName(ruleParam.getOutDfName())
    // metric writer
    val metricSteps = ruleParam
      .getOutputOpt(MetricOutputType)
      .map { metric =>
        MetricWriteStep(metric.getNameOpt.getOrElse(name), name, metric.getFlatten)
      }
      .toSeq
    // record writer
    val recordSteps = ruleParam
      .getOutputOpt(RecordOutputType)
      .map { record =>
        RecordWriteStep(record.getNameOpt.getOrElse(name), name)
      }
      .toSeq
    // update writer
    val dsCacheUpdateSteps = ruleParam
      .getOutputOpt(DscUpdateOutputType)
      .map { dsCacheUpdate =>
        DataSourceUpdateWriteStep(dsCacheUpdate.getNameOpt.getOrElse(""), name)
      }
      .toSeq

    metricSteps ++ recordSteps ++ dsCacheUpdateSteps
  }

}
