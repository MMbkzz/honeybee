
package com.stackstech.honeybee.bees.context

/**
 * context id, unique by different timestamp and tag
 */
case class ContextId(timestamp: Long, tag: String = "") extends Serializable {
  def id: String = {
    if (tag.nonEmpty) s"${tag}_$timestamp" else s"$timestamp"
  }
}
