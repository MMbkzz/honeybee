
package com.stackstech.honeybee.bees.configuration.enums

/**
 * the strategy to flatten metric
 *  <li> -  default flatten strategy
 *                                     metrics contains 1 row -> flatten metric json map
 *                                     metrics contains n > 1 rows -> flatten metric json array
 *                                     n = 0: { }
 *                                     n = 1: { "col1": "value1", "col2": "value2", ... }
 *                                     n > 1: { "arr-name": [ { "col1": "value1", "col2": "value2", ... }, ... ] }
 *                                     all rows
 *  </li>
 *  <li> - metrics contains n rows -> flatten metric json map
 *                                    n = 0: { }
 *                                    n >= 1: { "col1": "value1", "col2": "value2", ... }
 *                                    the first row only
 *  </li>
 *  <li> -   metrics contains n rows -> flatten metric json array
 *                                    n = 0: { "arr-name": [ ] }
 *                                    n >= 1: { "arr-name": [ { "col1": "value1", "col2": "value2", ... }, ... ] }
 *                                    all rows
 *  </li>
 *  <li> - metrics contains n rows -> flatten metric json wrapped map
 *                                n = 0: { "map-name": { } }
 *                                n >= 1: { "map-name": { "col1": "value1", "col2": "value2", ... } }
 *                                the first row only
 *  </li>
 */
object FlattenType extends GriffinEnum {
  type FlattenType = Value

  val DefaultFlattenType, EntriesFlattenType, ArrayFlattenType, MapFlattenType =
    Value

  val List, Array, Entries, Map, Default = Value

  override def withNameWithDefault(name: String): Value = {
    val flattenType = super.withNameWithDefault(name)
    flattenType match {
      case Array | List => ArrayFlattenType
      case Map => MapFlattenType
      case Entries => EntriesFlattenType
      case _ => DefaultFlattenType
    }
  }
}
