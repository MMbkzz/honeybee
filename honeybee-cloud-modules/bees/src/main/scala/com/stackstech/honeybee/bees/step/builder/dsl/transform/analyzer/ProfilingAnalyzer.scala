
package com.stackstech.honeybee.bees.step.builder.dsl.transform.analyzer

import com.stackstech.honeybee.bees.step.builder.dsl.expr._

case class ProfilingAnalyzer(expr: ProfilingClause, sourceName: String) extends BasicAnalyzer {

  val dataSourceNames: Set[String] =
    expr.preOrderTraverseDepthFirst(Set[String]())(seqDataSourceNames, combDataSourceNames)

  val selectionExprs: Seq[Expr] = {
    expr.selectClause.exprs.map(_.extractSelf).flatMap { expr =>
      expr match {
        case e: SelectionExpr => Some(e)
        case e: FunctionExpr => Some(e)
        case _ => None
      }
    }
  }

  val groupbyExprOpt: Option[GroupbyClause] = expr.groupbyClauseOpt
  val preGroupbyExprs: Seq[Expr] = expr.preGroupbyClauses.map(_.extractSelf)
  val postGroupbyExprs: Seq[Expr] = expr.postGroupbyClauses.map(_.extractSelf)

}
