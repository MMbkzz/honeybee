
package com.stackstech.honeybee.bees.context

import scala.collection.mutable.{Map => MutableMap}

/**
 * wrap metrics into one, each calculation produces one metric map
 */
case class MetricWrapper(name: String, applicationId: String) extends Serializable {

  val _Name = "name"
  val _Timestamp = "tmst"
  val _Value = "value"
  val _Metadata = "metadata"

  val metrics: MutableMap[Long, Map[String, Any]] = MutableMap()

  def insertMetric(timestamp: Long, value: Map[String, Any]): Unit = {
    val newValue = metrics.get(timestamp) match {
      case Some(v) => v ++ value
      case _ => value
    }
    metrics += (timestamp -> newValue)
  }

  def flush: Map[Long, Map[String, Any]] = {
    metrics.toMap.map { pair =>
      val (timestamp, value) = pair
      (
        timestamp,
        Map[String, Any](
          _Name -> name,
          _Timestamp -> timestamp,
          _Value -> value,
          _Metadata -> Map("applicationId" -> applicationId)))
    }
  }

}
