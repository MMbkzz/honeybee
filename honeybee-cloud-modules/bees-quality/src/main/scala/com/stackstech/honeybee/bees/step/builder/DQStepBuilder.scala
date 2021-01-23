
package com.stackstech.honeybee.bees.step.builder

import org.apache.commons.lang.StringUtils

import com.stackstech.honeybee.bees.Loggable
import com.stackstech.honeybee.bees.configuration.dqdefinition.{DataSourceParam, Param, RuleParam}
import com.stackstech.honeybee.bees.configuration.enums.DslType._
import com.stackstech.honeybee.bees.configuration.enums.ProcessType._
import com.stackstech.honeybee.bees.context.DQContext
import com.stackstech.honeybee.bees.step._

/**
 * build dq step by param
 */
trait DQStepBuilder extends Loggable with Serializable {

  type ParamType <: Param

  def buildDQStep(context: DQContext, param: ParamType): Option[DQStep]

  protected def getStepName(name: String): String = {
    if (StringUtils.isNotBlank(name)) name
    else DQStepNameGenerator.genName
  }

}

object DQStepBuilder {

  def buildStepOptByDataSourceParam(
      context: DQContext,
      dsParam: DataSourceParam): Option[DQStep] = {
    getDataSourceParamStepBuilder(context.procType)
      .flatMap(_.buildDQStep(context, dsParam))
  }

  private def getDataSourceParamStepBuilder(
      procType: ProcessType): Option[DataSourceParamStepBuilder] = {
    procType match {
      case BatchProcessType => Some(BatchDataSourceStepBuilder())
      case StreamingProcessType => Some(StreamingDataSourceStepBuilder())
      case _ => None
    }
  }

  def buildStepOptByRuleParam(context: DQContext, ruleParam: RuleParam): Option[DQStep] = {
    val dslType = ruleParam.getDslType
    val dsNames = context.dataSourceNames
    val funcNames = context.functionNames
    val dqStepOpt = getRuleParamStepBuilder(dslType, dsNames, funcNames)
      .flatMap(_.buildDQStep(context, ruleParam))
    dqStepOpt.toSeq
      .flatMap(_.getNames)
      .foreach(name => context.compileTableRegister.registerTable(name))
    dqStepOpt
  }

  private def getRuleParamStepBuilder(
      dslType: DslType,
      dsNames: Seq[String],
      funcNames: Seq[String]): Option[RuleParamStepBuilder] = {
    dslType match {
      case SparkSql => Some(SparkSqlDQStepBuilder())
      case DataFrameOpsType => Some(DataFrameOpsDQStepBuilder())
      case GriffinDsl => Some(GriffinDslDQStepBuilder(dsNames, funcNames))
      case _ => None
    }
  }

}
