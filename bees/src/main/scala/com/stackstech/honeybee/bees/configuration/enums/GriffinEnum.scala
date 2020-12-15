
package com.stackstech.honeybee.bees.configuration.enums

trait GriffinEnum extends Enumeration {
  type GriffinEnum = Value

  val Unknown: Value = Value

  /**
   *
   * @param name Constant value in String
   * @return Enum constant value
   */
  def withNameWithDefault(name: String): Value =
    values
      .find(_.toString.toLowerCase == name.replace("-", "").toLowerCase())
      .getOrElse(Unknown)

}
