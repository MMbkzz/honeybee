
package com.stackstech.honeybee.bees.step.builder.dsl.transform.analyzer

import com.stackstech.honeybee.bees.step.builder.dsl.expr._

case class TimelinessAnalyzer(expr: TimelinessClause, sourceName: String) extends BasicAnalyzer {

//  val tsExpr = expr.desc

//  val seqAlias = (expr: Expr, v: Seq[String]) => {
//    expr match {
//      case apr: AliasableExpr => v ++ apr.alias
//      case _ => v
//    }
//  }
//  val combAlias = (a: Seq[String], b: Seq[String]) => a ++ b
//
//  private val exprs = expr.exprs.toList
//  val selectionPairs = exprs.map { pr =>
//    val res = pr.preOrderTraverseDepthFirst(Seq[String]())(seqAlias, combAlias)
//    println(res)
//    println(pr)
//    (pr, res.headOption)
//  }
//
//  val (tsExprPair, endTsPairOpt) = selectionPairs match {
//    case Nil => throw new Exception(s"timeliness analyzer error: ts column not set")
//    case tsPair :: Nil => (tsPair, None)
//    case tsPair :: endTsPair :: _ => (tsPair, Some(endTsPair))
//  }
//
//  def getSelAlias(pair: (Expr, Option[String]), defAlias: String): (String, String) = {
//    val (pr, aliasOpt) = pair
//    val alias = aliasOpt.getOrElse(defAlias)
//    (pr.desc, alias)
//  }

  private val exprs = expr.exprs.map(_.desc).toList

  val (btsExpr, etsExprOpt) = exprs match {
    case Nil => throw new Exception("timeliness analyzer error: ts column not set")
    case btsExpr :: Nil => (btsExpr, None)
    case btsExpr :: etsExpr :: _ => (btsExpr, Some(etsExpr))
  }

}
