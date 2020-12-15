
package com.stackstech.honeybee.bees.datasource.connector.batch

import org.apache.spark.sql.{DataFrame, SparkSession}

import com.stackstech.honeybee.bees.configuration.dqdefinition.DataConnectorParam
import com.stackstech.honeybee.bees.context.TimeRange
import com.stackstech.honeybee.bees.datasource.TimestampStorage
import com.stackstech.honeybee.bees.utils.HdfsUtil
import com.stackstech.honeybee.bees.utils.ParamUtil._

/**
 * batch data connector for directory with text format data in the nth depth sub-directories
 */
@deprecated(
  s"This class is deprecated. Use '${classOf[FileBasedDataConnector].getCanonicalName}'.",
  "0.6.0")
case class TextDirBatchDataConnector(
    @transient sparkSession: SparkSession,
    dcParam: DataConnectorParam,
    timestampStorage: TimestampStorage)
    extends BatchDataConnector {

  val config: Map[String, Any] = dcParam.getConfig

  val DirPath = "dir.path"
  val DataDirDepth = "data.dir.depth"
  val SuccessFile = "success.file"
  val DoneFile = "done.file"

  val dirPath: String = config.getString(DirPath, "")
  val dataDirDepth: Int = config.getInt(DataDirDepth, 0)
  val successFile: String = config.getString(SuccessFile, "_SUCCESS")
  val doneFile: String = config.getString(DoneFile, "_DONE")

  val ignoreFilePrefix = "_"

  private def dirExist(): Boolean = {
    HdfsUtil.existPath(dirPath)
  }

  def data(ms: Long): (Option[DataFrame], TimeRange) = {
    assert(dirExist(), s"Text dir $dirPath is not exists!")
    val dfOpt = {
      val dataDirs = listSubDirs(dirPath :: Nil, dataDirDepth, readable)
      // touch done file for read dirs
      dataDirs.foreach(dir => touchDone(dir))

      val validDataDirs = dataDirs.filter(dir => !emptyDir(dir))

      if (validDataDirs.nonEmpty) {
        val df = sparkSession.read.text(validDataDirs: _*)
        val dfOpt = Some(df)
        val preDfOpt = preProcess(dfOpt, ms)
        preDfOpt
      } else {
        None
      }
    }
    val tmsts = readTmst(ms)
    (dfOpt, TimeRange(ms, tmsts))
  }

  @scala.annotation.tailrec
  private def listSubDirs(
      paths: Seq[String],
      depth: Int,
      filteFunc: String => Boolean): Seq[String] = {
    val subDirs = paths.flatMap { path =>
      HdfsUtil.listSubPathsByType(path, "dir", fullPath = true)
    }
    if (depth <= 0) {
      subDirs.filter(filteFunc)
    } else {
      listSubDirs(subDirs, depth - 1, filteFunc)
    }
  }

  private def readable(dir: String): Boolean = isSuccess(dir) && !isDone(dir)
  private def isDone(dir: String): Boolean = HdfsUtil.existFileInDir(dir, doneFile)
  private def isSuccess(dir: String): Boolean = HdfsUtil.existFileInDir(dir, successFile)

  private def touchDone(dir: String): Unit =
    HdfsUtil.createEmptyFile(HdfsUtil.getHdfsFilePath(dir, doneFile))

  private def emptyDir(dir: String): Boolean = {
    HdfsUtil.listSubPathsByType(dir, "file").forall(_.startsWith(ignoreFilePrefix))
  }

//  def metaData(): Try[Iterable[(String, String)]] = {
//    Try {
//      val st = sparkSession.read.format("com.databricks.spark.avro").
  //       load(concreteFileFullPath).schema
//      st.fields.map(f => (f.name, f.dataType.typeName))
//    }
//  }

}
