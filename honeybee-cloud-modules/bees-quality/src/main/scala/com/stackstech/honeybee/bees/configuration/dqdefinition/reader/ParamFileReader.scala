
package com.stackstech.honeybee.bees.configuration.dqdefinition.reader

import scala.reflect.ClassTag
import scala.util.Try

import com.stackstech.honeybee.bees.configuration.dqdefinition.Param
import com.stackstech.honeybee.bees.utils.{HdfsUtil, JsonUtil}

/**
 * read params from config file path
 *
 * @param filePath:  hdfs path ("hdfs://cluster-name/path")
 *                   local file path ("file:///path")
 *                   relative file path ("relative/path")
 */
case class ParamFileReader(filePath: String) extends ParamReader {

  def readConfig[T <: Param](implicit m: ClassTag[T]): Try[T] = {
    Try {
      val source = HdfsUtil.openFile(filePath)
      val param = JsonUtil.fromJson[T](source)
      source.close()
      validate(param)
    }
  }

}
