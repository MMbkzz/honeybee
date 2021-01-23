
package com.stackstech.honeybee.bees.step.builder.dsl.transform.analyzer

import com.stackstech.honeybee.bees.step.builder.dsl.expr._

case class AccuracyAnalyzer(expr: LogicalExpr, sourceName: String, targetName: String)
    extends BasicAnalyzer {

  val dataSourceNames: Set[String] =
    expr.preOrderTraverseDepthFirst(Set[String]())(seqDataSourceNames, combDataSourceNames)

  val sourceSelectionExprs: Seq[SelectionExpr] = {
    val seq = seqSelectionExprs(sourceName)
    expr.preOrderTraverseDepthFirst(Seq[SelectionExpr]())(seq, combSelectionExprs)
  }
  val targetSelectionExprs: Seq[SelectionExpr] = {
    val seq = seqSelectionExprs(targetName)
    expr.preOrderTraverseDepthFirst(Seq[SelectionExpr]())(seq, combSelectionExprs)
  }

  val selectionExprs: Seq[AliasableExpr] = sourceSelectionExprs ++ {
    expr.preOrderTraverseDepthFirst(Seq[AliasableExpr]())(seqWithAliasExprs, combWithAliasExprs)
  }

}
