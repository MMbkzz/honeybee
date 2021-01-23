
package com.stackstech.honeybee.bees.configuration.dqdefinition.reader

import com.stackstech.honeybee.bees.utils.JsonUtil

object ParamReaderFactory {

  val json = "json"
  val file = "file"

  /**
   * parse string content to get param reader
   * @param pathOrJson
   * @return
   */
  def getParamReader(pathOrJson: String): ParamReader = {
    val strType = paramStrType(pathOrJson)
    if (json.equals(strType)) ParamJsonReader(pathOrJson)
    else ParamFileReader(pathOrJson)
  }

  private def paramStrType(str: String): String = {
    try {
      JsonUtil.toAnyMap(str)
      json
    } catch {
      case _: Throwable => file
    }
  }

}
