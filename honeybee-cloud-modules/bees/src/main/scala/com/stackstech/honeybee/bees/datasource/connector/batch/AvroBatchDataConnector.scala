
package com.stackstech.honeybee.bees.datasource.connector.batch

import org.apache.spark.sql.{DataFrame, SparkSession}

import com.stackstech.honeybee.bees.configuration.dqdefinition.DataConnectorParam
import com.stackstech.honeybee.bees.context.TimeRange
import com.stackstech.honeybee.bees.datasource.TimestampStorage
import com.stackstech.honeybee.bees.utils.HdfsUtil
import com.stackstech.honeybee.bees.utils.ParamUtil._

/**
 * batch data connector for avro file
 */
@deprecated(
  s"This class is deprecated. Use '${classOf[FileBasedDataConnector].getCanonicalName}'.",
  "0.6.0")
case class AvroBatchDataConnector(
    @transient sparkSession: SparkSession,
    dcParam: DataConnectorParam,
    timestampStorage: TimestampStorage)
    extends BatchDataConnector {

  val config: Map[String, Any] = dcParam.getConfig

  val FilePath = "file.path"
  val FileName = "file.name"

  val filePath: String = config.getString(FilePath, "")
  val fileName: String = config.getString(FileName, "")

  val concreteFileFullPath: String = if (pathPrefix()) filePath else fileName

  private def pathPrefix(): Boolean = {
    filePath.nonEmpty
  }

  private def fileExist(): Boolean = {
    HdfsUtil.existPath(concreteFileFullPath)
  }

  def data(ms: Long): (Option[DataFrame], TimeRange) = {
    assert(fileExist(), s"Avro file $concreteFileFullPath is not exists!")
    val dfOpt = {
      val df = sparkSession.read.format("com.databricks.spark.avro").load(concreteFileFullPath)
      val dfOpt = Some(df)
      val preDfOpt = preProcess(dfOpt, ms)
      preDfOpt
    }
    val tmsts = readTmst(ms)
    (dfOpt, TimeRange(ms, tmsts))
  }

}
