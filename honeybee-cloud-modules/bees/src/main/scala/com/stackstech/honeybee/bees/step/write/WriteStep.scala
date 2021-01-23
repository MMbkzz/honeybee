
package com.stackstech.honeybee.bees.step.write

import com.stackstech.honeybee.bees.step.DQStep

trait WriteStep extends DQStep {

  val inputName: String

  val writeTimestampOpt: Option[Long]

  override def getNames: Seq[String] = Nil

}
