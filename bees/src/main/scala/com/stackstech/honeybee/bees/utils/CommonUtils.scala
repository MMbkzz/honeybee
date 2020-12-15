
package com.stackstech.honeybee.bees.utils

import java.util.concurrent.TimeUnit

import com.stackstech.honeybee.bees.Loggable

object CommonUtils extends Loggable {

  /**
   * Executes a given code block and logs the time taken for its execution.
   *
   * @param f Arbitrary code block
   * @param timeUnit required for time conversion to desired unit. Default: [[TimeUnit.SECONDS]]
   * @tparam T resultant type parameter
   * @return result of type T
   */
  def timeThis[T](f: => T, timeUnit: TimeUnit = TimeUnit.SECONDS): T = {
    val startNanos = System.nanoTime()
    val result = f
    val endNanos = System.nanoTime()

    griffinLogger.info(s"Time taken: ${timeUnit
      .convert(endNanos - startNanos, TimeUnit.NANOSECONDS)} ${timeUnit.name().toLowerCase}")

    result
  }
}
