
package com.stackstech.honeybee.bees.configuration.dqdefinition.reader

import scala.reflect.ClassTag
import scala.util.Try

import com.stackstech.honeybee.bees.Loggable
import com.stackstech.honeybee.bees.configuration.dqdefinition.Param

trait ParamReader extends Loggable with Serializable {

  /**
   * read config param
   *
   * @tparam T     param type expected
   * @return       parsed param
   */
  def readConfig[T <: Param](implicit m: ClassTag[T]): Try[T]

  /**
   * validate config param
   *
   * @param param  param to be validated
   * @return       param itself
   */
  protected def validate[T <: Param](param: T): T = {
    param.validate()
    param
  }

}
