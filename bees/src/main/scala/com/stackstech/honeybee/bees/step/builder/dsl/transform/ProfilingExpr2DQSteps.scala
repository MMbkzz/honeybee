
package com.stackstech.honeybee.bees.step.builder.dsl.transform

import org.apache.commons.lang.StringUtils

import com.stackstech.honeybee.bees.configuration.dqdefinition.RuleParam
import com.stackstech.honeybee.bees.configuration.enums.FlattenType.DefaultFlattenType
import com.stackstech.honeybee.bees.configuration.enums.OutputType._
import com.stackstech.honeybee.bees.configuration.enums.ProcessType._
import com.stackstech.honeybee.bees.context.DQContext
import com.stackstech.honeybee.bees.step.DQStep
import com.stackstech.honeybee.bees.step.builder.ConstantColumns
import com.stackstech.honeybee.bees.step.builder.dsl.expr._
import com.stackstech.honeybee.bees.step.builder.dsl.transform.analyzer.ProfilingAnalyzer
import com.stackstech.honeybee.bees.step.transform.SparkSqlTransformStep
import com.stackstech.honeybee.bees.step.write.MetricWriteStep
import com.stackstech.honeybee.bees.utils.ParamUtil._

/**
 * generate profiling dq steps
 */
case class ProfilingExpr2DQSteps(context: DQContext, expr: Expr, ruleParam: RuleParam)
    extends Expr2DQSteps {

  private object ProfilingKeys {
    val _source = "source"
  }
  import ProfilingKeys._

  def getDQSteps: Seq[DQStep] = {
    val details = ruleParam.getDetails
    val profilingExpr = expr.asInstanceOf[ProfilingClause]

    val sourceName = profilingExpr.fromClauseOpt match {
      case Some(fc) => fc.dataSource
      case _ => details.getString(_source, context.getDataSourceName(0))
    }
    val fromClause = profilingExpr.fromClauseOpt.getOrElse(FromClause(sourceName)).desc

    val procType = context.procType
    val timestamp = context.contextId.timestamp

    if (!context.runTimeTableRegister.existsTable(sourceName)) {
      warn(s"[$timestamp] data source $sourceName not exists")
      Nil
    } else {
      val analyzer = ProfilingAnalyzer(profilingExpr, sourceName)
      val selExprDescs = analyzer.selectionExprs.map { sel =>
        val alias = sel match {
          case s: AliasableExpr =>
            s.alias.filter(StringUtils.isNotEmpty).map(a => s" AS `$a`").getOrElse("")

          case _ => ""
        }
        s"${sel.desc}$alias"
      }
      val selCondition = profilingExpr.selectClause.extraConditionOpt.map(_.desc).mkString
      val selClause = procType match {
        case BatchProcessType => selExprDescs.mkString(", ")
        case StreamingProcessType => (s"`${ConstantColumns.tmst}`" +: selExprDescs).mkString(", ")
      }
      val groupByClauseOpt = analyzer.groupbyExprOpt
      val groupbyClause = procType match {
        case BatchProcessType => groupByClauseOpt.map(_.desc).getOrElse("")
        case StreamingProcessType =>
          val tmstGroupbyClause =
            GroupbyClause(LiteralStringExpr(s"`${ConstantColumns.tmst}`") :: Nil, None)
          val mergedGroubbyClause = tmstGroupbyClause.merge(groupByClauseOpt match {
            case Some(gbc) => gbc
            case _ => GroupbyClause(Nil, None)
          })
          mergedGroubbyClause.desc
      }
      val preGroupbyClause = analyzer.preGroupbyExprs.map(_.desc).mkString(" ")
      val postGroupbyClause = analyzer.postGroupbyExprs.map(_.desc).mkString(" ")

      // 1. select statement
      val profilingSql = {
        s"SELECT $selCondition $selClause " +
          s"$fromClause $preGroupbyClause $groupbyClause $postGroupbyClause"
      }
      val profilingName = ruleParam.getOutDfName()
      val profilingMetricWriteStep = {
        val metricOpt = ruleParam.getOutputOpt(MetricOutputType)
        val mwName = metricOpt.flatMap(_.getNameOpt).getOrElse(ruleParam.getOutDfName())
        val flattenType = metricOpt.map(_.getFlatten).getOrElse(DefaultFlattenType)
        MetricWriteStep(mwName, profilingName, flattenType)
      }
      val profilingTransStep =
        SparkSqlTransformStep(
          profilingName,
          profilingSql,
          details,
          Some(profilingMetricWriteStep))
      profilingTransStep :: Nil
    }
  }

}
