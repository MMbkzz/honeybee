
package com.stackstech.honeybee.bees.step.builder

import com.stackstech.honeybee.bees.configuration.dqdefinition.{DataConnectorParam, DataSourceParam}
import com.stackstech.honeybee.bees.context.DQContext
import com.stackstech.honeybee.bees.step.DQStep
import com.stackstech.honeybee.bees.step.read._

/**
 * build dq step by data source param
 */
trait DataSourceParamStepBuilder extends DQStepBuilder {

  type ParamType = DataSourceParam

  def buildDQStep(context: DQContext, param: ParamType): Option[DQStep] = {
    val name = getStepName(param.getName)

    param.getConnector match {
      case Some(dc) =>
        val steps = buildReadSteps(context, dc)
        if (steps.isDefined) Some(UnionReadStep(name, Seq(steps.get)))
        else None
      case _ => None
    }
  }

  protected def buildReadSteps(context: DQContext, dcParam: DataConnectorParam): Option[ReadStep]

}
