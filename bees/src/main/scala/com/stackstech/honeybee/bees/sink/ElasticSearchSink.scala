
package com.stackstech.honeybee.bees.sink

import scala.concurrent.Future

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.DataFrame

import com.stackstech.honeybee.bees.utils.{HttpUtil, JsonUtil, TimeUtil}
import com.stackstech.honeybee.bees.utils.ParamUtil._

/**
 * sink metric and record through http request
 */
case class ElasticSearchSink(
    config: Map[String, Any],
    jobName: String,
    timeStamp: Long,
    block: Boolean)
    extends Sink {

  val Api = "api"
  val Method = "method"
  val ConnectionTimeout = "connection.timeout"
  val Retry = "retry"

  val api: String = config.getString(Api, "")
  val method: String = config.getString(Method, "post")

  val connectionTimeout: Long =
    TimeUtil.milliseconds(config.getString(ConnectionTimeout, "")).getOrElse(-1L)

  val retry: Int = config.getInt(Retry, 10)

  val _Value = "value"

  def validate(): Boolean = {
    api.nonEmpty
  }

  private def httpResult(dataMap: Map[String, Any]): Unit = {
    try {
      val data = JsonUtil.toJson(dataMap)
      // http request
      val params = Map[String, Object]()
      val header = Map[String, Object](("Content-Type", "application/json"))

      def func(): (Long, Future[Boolean]) = {
        import scala.concurrent.ExecutionContext.Implicits.global
        (timeStamp, Future(HttpUtil.doHttpRequest(api, method, params, header, data)))
      }
      if (block) SinkTaskRunner.addBlockTask(func _, retry, connectionTimeout)
      else SinkTaskRunner.addNonBlockTask(func _, retry)
    } catch {
      case e: Throwable => error(e.getMessage, e)
    }

  }

  override def sinkMetrics(metrics: Map[String, Any]): Unit = {
    httpResult(metrics)
  }

  override def sinkBatchRecords(dataset: DataFrame, key: Option[String] = None): Unit = {}
}
