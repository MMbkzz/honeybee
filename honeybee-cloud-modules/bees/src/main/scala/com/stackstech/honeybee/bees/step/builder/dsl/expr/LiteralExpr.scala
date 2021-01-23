
package com.stackstech.honeybee.bees.step.builder.dsl.expr

import com.stackstech.honeybee.bees.utils.TimeUtil

trait LiteralExpr extends Expr {
  def coalesceDesc: String = desc
}

case class LiteralNullExpr(str: String) extends LiteralExpr {
  def desc: String = "NULL"
}

case class LiteralNanExpr(str: String) extends LiteralExpr {
  def desc: String = "NaN"
}

case class LiteralStringExpr(str: String) extends LiteralExpr {
  def desc: String = str
}

case class LiteralNumberExpr(str: String) extends LiteralExpr {
  def desc: String = {
    try {
      if (str.contains(".")) {
        str.toDouble.toString
      } else {
        str.toLong.toString
      }
    } catch {
      case _: Throwable => throw new Exception(s"$str is invalid number")
    }
  }
}

case class LiteralTimeExpr(str: String) extends LiteralExpr {
  def desc: String = {
    TimeUtil.milliseconds(str) match {
      case Some(t) => t.toString
      case _ => throw new Exception(s"$str is invalid time")
    }
  }
}

case class LiteralBooleanExpr(str: String) extends LiteralExpr {
  final val TrueRegex = """(?i)true""".r
  final val FalseRegex = """(?i)false""".r
  def desc: String = {
    str match {
      case TrueRegex() => true.toString
      case FalseRegex() => false.toString
      case _ => throw new Exception(s"$str is invalid boolean")
    }
  }
}
