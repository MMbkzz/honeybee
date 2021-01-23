
package com.stackstech.honeybee.bees.configuration.dqdefinition

trait Param extends Serializable {

  /**
   * validate param internally
   */
  def validate(): Unit

}
