
package com.stackstech.honeybee.bees.step.builder

import java.util.concurrent.atomic.AtomicLong

object DQStepNameGenerator {
  private val counter: AtomicLong = new AtomicLong(0L)
  private val head: String = "step"

  def genName: String = {
    s"$head$increment"
  }

  private def increment: Long = {
    counter.incrementAndGet()
  }
}
