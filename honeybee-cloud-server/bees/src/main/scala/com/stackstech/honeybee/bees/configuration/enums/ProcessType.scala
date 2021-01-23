
package com.stackstech.honeybee.bees.configuration.enums

import com.stackstech.honeybee.bees.configuration.enums

/**
 * process type enum
 *  <li> - Process in batch mode </li>
 *  <li> - Process in streaming mode</li>
 */
object ProcessType extends GriffinEnum {
  type ProcessType = Value

  val BatchProcessType: enums.ProcessType.Value = Value("Batch")
  val StreamingProcessType: enums.ProcessType.Value = Value("Streaming")
}
