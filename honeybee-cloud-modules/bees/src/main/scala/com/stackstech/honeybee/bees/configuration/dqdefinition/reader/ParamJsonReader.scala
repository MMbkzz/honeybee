
package com.stackstech.honeybee.bees.configuration.dqdefinition.reader

import scala.reflect.ClassTag
import scala.util.Try

import com.stackstech.honeybee.bees.configuration.dqdefinition.Param
import com.stackstech.honeybee.bees.utils.JsonUtil

/**
 * read params from json string directly
 *
 * @param jsonString
 */
case class ParamJsonReader(jsonString: String) extends ParamReader {

  def readConfig[T <: Param](implicit m: ClassTag[T]): Try[T] = {
    Try {
      val param = JsonUtil.fromJson[T](jsonString)
      validate(param)
    }
  }

}
