
package com.stackstech.honeybee.bees.utils

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._

object DataFrameUtil {

  def unionDfOpts(dfOpt1: Option[DataFrame], dfOpt2: Option[DataFrame]): Option[DataFrame] = {
    (dfOpt1, dfOpt2) match {
      case (Some(df1), Some(df2)) => Some(unionByName(df1, df2))
      case (Some(_), _) => dfOpt1
      case (_, Some(_)) => dfOpt2
      case _ => None
    }
  }

  def unionByName(a: DataFrame, b: DataFrame): DataFrame = {
    val columns = a.columns.toSet.intersect(b.columns.toSet).map(col).toSeq
    a.select(columns: _*).union(b.select(columns: _*))
  }

}
