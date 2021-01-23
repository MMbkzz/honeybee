
package com.stackstech.honeybee.bees.configuration.enums

/**
 * the strategy to output metric
 *  <li> - output the rule step result as metric</li>
 *  <li> - output the rule step result as records</li>
 *  <li> - output the rule step result to update data source cache</li>
 *  <li> - will not output the result </li>
 */
object OutputType extends GriffinEnum {
  type OutputType = Value

  val MetricOutputType, RecordOutputType, DscUpdateOutputType, UnknownOutputType = Value

  val Metric, Record, Records, DscUpdate = Value

  override def withNameWithDefault(name: String): Value = {
    val flattenType = super.withNameWithDefault(name)
    flattenType match {
      case Metric => MetricOutputType
      case Record | Records => RecordOutputType
      case DscUpdate => DscUpdateOutputType
      case _ => UnknownOutputType
    }
  }
}
