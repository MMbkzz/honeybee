
package com.stackstech.honeybee.bees.context

import scala.collection.mutable

import org.apache.spark.sql.DataFrame

import com.stackstech.honeybee.bees.Loggable

/**
 * cache and unpersist dataframes
 */
case class DataFrameCache() extends Loggable {

  val dataFrames: mutable.Map[String, DataFrame] = mutable.Map()
  val trashDataFrames: mutable.MutableList[DataFrame] = mutable.MutableList()

  private def trashDataFrame(df: DataFrame): Unit = {
    trashDataFrames += df
  }
  private def trashDataFrames(dfs: Seq[DataFrame]): Unit = {
    trashDataFrames ++= dfs
  }

  def cacheDataFrame(name: String, df: DataFrame): Unit = {
    info(s"try to cache data frame $name")
    dataFrames.get(name) match {
      case Some(odf) =>
        trashDataFrame(odf)
        dataFrames += (name -> df)
        df.cache
        info("cache after replace old df")
      case _ =>
        dataFrames += (name -> df)
        df.cache
        info("cache after replace no old df")
    }
  }

  def uncacheDataFrame(name: String): Unit = {
    dataFrames.get(name).foreach(df => trashDataFrame(df))
    dataFrames -= name
  }
  def uncacheAllDataFrames(): Unit = {
    trashDataFrames(dataFrames.values.toSeq)
    dataFrames.clear
  }

  def clearAllTrashDataFrames(): Unit = {
    trashDataFrames.foreach(_.unpersist)
    trashDataFrames.clear
  }

}
