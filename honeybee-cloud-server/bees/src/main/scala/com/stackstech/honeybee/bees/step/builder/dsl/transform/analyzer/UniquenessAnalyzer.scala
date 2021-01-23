
package com.stackstech.honeybee.bees.step.builder.dsl.transform.analyzer

import com.stackstech.honeybee.bees.step.builder.dsl.expr.{AliasableExpr, _}

case class UniquenessAnalyzer(expr: UniquenessClause, sourceName: String, targetName: String)
    extends BasicAnalyzer {

  val seqAlias: (Expr, Seq[String]) => Seq[String] = (expr: Expr, v: Seq[String]) => {
    expr match {
      case apr: AliasableExpr => v ++ apr.alias
      case _ => v
    }
  }
  val combAlias: (Seq[String], Seq[String]) => Seq[String] = (a: Seq[String], b: Seq[String]) =>
    a ++ b

  private val exprs = expr.exprs
  private def genAlias(idx: Int): String = s"alias_$idx"
  val selectionPairs: Seq[(Expr, String)] = exprs.zipWithIndex.map { pair =>
    val (pr, idx) = pair
    val res = pr.preOrderTraverseDepthFirst(Seq[String]())(seqAlias, combAlias)
    (pr, res.headOption.getOrElse(genAlias(idx)))
  }

  if (selectionPairs.isEmpty) {
    throw new Exception("uniqueness analyzer error: empty selection")
  }

}
