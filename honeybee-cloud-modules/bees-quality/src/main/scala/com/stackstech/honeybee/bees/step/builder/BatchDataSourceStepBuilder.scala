
package com.stackstech.honeybee.bees.step.builder

import com.stackstech.honeybee.bees.configuration.dqdefinition._
import com.stackstech.honeybee.bees.context.DQContext
import com.stackstech.honeybee.bees.step.read.ReadStep

case class BatchDataSourceStepBuilder() extends DataSourceParamStepBuilder {

  def buildReadSteps(context: DQContext, dcParam: DataConnectorParam): Option[ReadStep] = {
    None
  }

}
