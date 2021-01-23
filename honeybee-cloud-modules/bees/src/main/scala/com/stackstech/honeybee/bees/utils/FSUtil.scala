
package com.stackstech.honeybee.bees.utils

import java.io.File
import java.net.URI

import scala.collection.mutable.{Map => MutableMap}

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem

import com.stackstech.honeybee.bees.Loggable

object FSUtil extends Loggable {

  private val fsMap: MutableMap[String, FileSystem] = MutableMap()
  private val defaultFS: FileSystem = FileSystem.get(getConfiguration)

  def getFileSystem(path: String): FileSystem = {
    getUriOpt(path) match {
      case Some(uri) =>
        fsMap.get(uri.getScheme) match {
          case Some(fs) => fs
          case _ =>
            val fs = try {
              FileSystem.get(uri, getConfiguration)
            } catch {
              case e: Throwable =>
                error(s"get file system error: ${e.getMessage}", e)
                throw e
            }
            fsMap += (uri.getScheme -> fs)
            fs
        }
      case _ => defaultFS
    }
  }

  private def getConfiguration: Configuration = {
    val conf = new Configuration()
    conf.setBoolean("dfs.support.append", true)
//    conf.set("fs.defaultFS", "hdfs://localhost")    // debug in hdfs localhost env
    conf
  }

  private def getUriOpt(path: String): Option[URI] = {
    val uriOpt = try {
      Some(new URI(path))
    } catch {
      case _: Throwable => None
    }
    uriOpt.flatMap { uri =>
      if (uri.getScheme == null) {
        try {
          Some(new File(path).toURI)
        } catch {
          case _: Throwable => None
        }
      } else Some(uri)
    }
  }

}
