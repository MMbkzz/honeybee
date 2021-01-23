
package com.stackstech.honeybee.bees.context

import scala.collection.mutable.{Set => MutableSet}

import org.apache.spark.sql._

import com.stackstech.honeybee.bees.Loggable

/**
 * register table name
 */
trait TableRegister extends Loggable with Serializable {

  protected val tables: MutableSet[String] = MutableSet()

  def registerTable(name: String): Unit = {
    tables += name
  }

  def existsTable(name: String): Boolean = {
    tables.exists(_.equals(name))
  }

  def unregisterTable(name: String): Unit = {
    if (existsTable(name)) tables -= name
  }
  def unregisterAllTables(): Unit = {
    tables.clear
  }

  def getTables: Set[String] = {
    tables.toSet
  }

}

/**
 * register table name when building dq job
 */
case class CompileTableRegister() extends TableRegister {}

/**
 * register table name and create temp view during calculation
 */
case class RunTimeTableRegister(@transient sparkSession: SparkSession) extends TableRegister {

  def registerTable(name: String, df: DataFrame): Unit = {
    registerTable(name)
    df.createOrReplaceTempView(name)
  }

  override def unregisterTable(name: String): Unit = {
    if (existsTable(name)) {
      sparkSession.catalog.dropTempView(name)
      tables -= name
    }
  }
  override def unregisterAllTables(): Unit = {
    val uts = getTables
    uts.foreach(t => sparkSession.catalog.dropTempView(t))
    tables.clear
  }

}
