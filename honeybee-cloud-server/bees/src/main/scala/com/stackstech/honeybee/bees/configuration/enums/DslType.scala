
package com.stackstech.honeybee.bees.configuration.enums

import com.stackstech.honeybee.bees.configuration.enums

/**
 * dsl type indicates the language type of rule param
 * <li> - spark-sql: rule defined in "SPARK-SQL" directly</li>
 * <li> - df-ops|df-opr|: data frame operations rule, support some pre-defined data frame ops()</li>
 * <li> - griffin dsl rule, to define dq measurements easier</li>
 */
object DslType extends GriffinEnum {
  type DslType = Value

  val SparkSql, DfOps, DfOpr, DfOperations, GriffinDsl, DataFrameOpsType = Value

  /**
   *
   * @param name Dsltype from config file
   * @return Enum value corresponding to string
   */
  def withNameWithDslType(name: String): Value =
    values
      .find(_.toString.toLowerCase == name.replace("-", "").toLowerCase())
      .getOrElse(GriffinDsl)

  override def withNameWithDefault(name: String): enums.DslType.Value = {
    val dslType = withNameWithDslType(name)
    dslType match {
      case DfOps | DfOpr | DfOperations => DataFrameOpsType
      case _ => dslType
    }
  }
}
