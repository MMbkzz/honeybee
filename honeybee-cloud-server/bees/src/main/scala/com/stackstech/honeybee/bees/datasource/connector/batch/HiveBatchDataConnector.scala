
package com.stackstech.honeybee.bees.datasource.connector.batch

import org.apache.spark.sql.{DataFrame, SparkSession}

import com.stackstech.honeybee.bees.configuration.dqdefinition.DataConnectorParam
import com.stackstech.honeybee.bees.context.TimeRange
import com.stackstech.honeybee.bees.datasource.TimestampStorage
import com.stackstech.honeybee.bees.utils.ParamUtil._

/**
 * batch data connector for hive table
 */
case class HiveBatchDataConnector(
    @transient sparkSession: SparkSession,
    dcParam: DataConnectorParam,
    timestampStorage: TimestampStorage)
    extends BatchDataConnector {

  val config: Map[String, Any] = dcParam.getConfig

  val Database = "database"
  val TableName = "table.name"
  val Where = "where"

  val database: String = config.getString(Database, "default")
  val tableName: String = config.getString(TableName, "")
  val whereString: String = config.getString(Where, "")

  val concreteTableName = s"$database.$tableName"
  val wheres: Array[String] = whereString.split(",").map(_.trim).filter(_.nonEmpty)

  def data(ms: Long): (Option[DataFrame], TimeRange) = {
    val dfOpt = {
      val dtSql = dataSql()
      info(dtSql)
      val df = sparkSession.sql(dtSql)
      val dfOpt = Some(df)
      val preDfOpt = preProcess(dfOpt, ms)
      preDfOpt
    }
    val tmsts = readTmst(ms)
    (dfOpt, TimeRange(ms, tmsts))
  }

  private def dataSql(): String = {
    val tableClause = s"SELECT * FROM $concreteTableName"
    if (wheres.length > 0) {
      val clauses = wheres.map { w =>
        s"$tableClause WHERE $w"
      }
      clauses.mkString(" UNION ALL ")
    } else tableClause
  }

}
