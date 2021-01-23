
package com.stackstech.honeybee.bees.step.write

import scala.collection.mutable.ArrayBuffer

import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{ArrayType, DataType, StructField, StructType}

/**
 * spark row formatter
 */
object SparkRowFormatter {

  def formatRow(row: Row): Map[String, Any] = {
    formatRowWithSchema(row, row.schema)
  }

  private def formatRowWithSchema(row: Row, schema: StructType): Map[String, Any] = {
    formatStruct(schema.fields, row)
  }

  private def formatStruct(schema: Seq[StructField], r: Row) = {
    val paired = schema.zip(r.toSeq)
    paired.foldLeft(Map[String, Any]())((s, p) => s ++ formatItem(p))
  }

  private def formatItem(p: (StructField, Any)): Map[String, Any] = {
    p match {
      case (sf, a) =>
        sf.dataType match {
          case ArrayType(et, _) =>
            Map(
              sf.name ->
                (if (a == null) a else formatArray(et, a.asInstanceOf[ArrayBuffer[Any]])))
          case StructType(s) =>
            Map(sf.name -> (if (a == null) a else formatStruct(s, a.asInstanceOf[Row])))
          case _ => Map(sf.name -> a)
        }
    }
  }

  private def formatArray(et: DataType, arr: ArrayBuffer[Any]): Seq[Any] = {
    et match {
      case StructType(s) => arr.map(e => formatStruct(s, e.asInstanceOf[Row]))
      case ArrayType(t, _) =>
        arr.map(e => formatArray(t, e.asInstanceOf[ArrayBuffer[Any]]))
      case _ => arr
    }
  }
}
