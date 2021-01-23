
package com.stackstech.honeybee.bees.configuration.enums

/**
 * write mode when write metrics and records
 */
sealed trait WriteMode {}

object WriteMode {
  def defaultMode(procType: ProcessType.ProcessType): WriteMode = {
    procType match {
      case ProcessType.BatchProcessType => SimpleMode
      case ProcessType.StreamingProcessType => TimestampMode
    }
  }
}

/**
 * simple mode: write metrics and records directly
 */
case object SimpleMode extends WriteMode {}

/**
 * timestamp mode: write metrics and records with timestamp information
 */
case object TimestampMode extends WriteMode {}
