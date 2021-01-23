
package com.stackstech.honeybee.bees.step.builder.dsl.transform

import com.stackstech.honeybee.bees.Loggable
import com.stackstech.honeybee.bees.configuration.dqdefinition.RuleParam
import com.stackstech.honeybee.bees.configuration.enums.DqType._
import com.stackstech.honeybee.bees.context.DQContext
import com.stackstech.honeybee.bees.step.DQStep
import com.stackstech.honeybee.bees.step.builder.dsl.expr.Expr
import com.stackstech.honeybee.bees.step.write.{MetricWriteStep, RecordWriteStep, WriteStep}

trait Expr2DQSteps extends Loggable with Serializable {

  protected val emtptDQSteps: Seq[DQStep] = Seq[DQStep]()
  protected val emptyMap: Map[String, Any] = Map[String, Any]()

  def getDQSteps: Seq[DQStep]
}

/**
 * get dq steps generator for griffin dsl rule
 */
object Expr2DQSteps {
  private val emtptExpr2DQSteps: Expr2DQSteps = new Expr2DQSteps {
    def getDQSteps: Seq[DQStep] = emtptDQSteps
  }

  def apply(context: DQContext, expr: Expr, ruleParam: RuleParam): Expr2DQSteps = {
    ruleParam.getDqType match {
      case Accuracy => AccuracyExpr2DQSteps(context, expr, ruleParam)
      case Profiling => ProfilingExpr2DQSteps(context, expr, ruleParam)
      case Uniqueness => UniquenessExpr2DQSteps(context, expr, ruleParam)
      case Distinct => DistinctnessExpr2DQSteps(context, expr, ruleParam)
      case Timeliness => TimelinessExpr2DQSteps(context, expr, ruleParam)
      case Completeness => CompletenessExpr2DQSteps(context, expr, ruleParam)
      case _ => emtptExpr2DQSteps
    }
  }
}
