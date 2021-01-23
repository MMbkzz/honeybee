
package com.stackstech.honeybee.bees.step.builder.dsl.transform.analyzer

import com.stackstech.honeybee.bees.step.builder.dsl.expr._

/**
 * analyzer of expr, to help generate dq steps by expr
 */
trait BasicAnalyzer extends Serializable {

  val expr: Expr

  val seqDataSourceNames: (Expr, Set[String]) => Set[String] = (expr: Expr, v: Set[String]) => {
    expr match {
      case DataSourceHeadExpr(name) => v + name
      case _ => v
    }
  }
  val combDataSourceNames: (Set[String], Set[String]) => Set[String] =
    (a: Set[String], b: Set[String]) => a ++ b

  val seqSelectionExprs: String => (Expr, Seq[SelectionExpr]) => Seq[SelectionExpr] =
    (dsName: String) =>
      (expr: Expr, v: Seq[SelectionExpr]) => {
        expr match {
          case se @ SelectionExpr(head: DataSourceHeadExpr, _, _) if head.name == dsName =>
            v :+ se
          case _ => v
        }
    }
  val combSelectionExprs: (Seq[SelectionExpr], Seq[SelectionExpr]) => Seq[SelectionExpr] =
    (a: Seq[SelectionExpr], b: Seq[SelectionExpr]) => a ++ b

  val seqWithAliasExprs: (Expr, Seq[AliasableExpr]) => Seq[AliasableExpr] =
    (expr: Expr, v: Seq[AliasableExpr]) => {
      expr match {
        case _: SelectExpr => v
        case a: AliasableExpr if a.alias.nonEmpty => v :+ a
        case _ => v
      }
    }
  val combWithAliasExprs: (Seq[AliasableExpr], Seq[AliasableExpr]) => Seq[AliasableExpr] =
    (a: Seq[AliasableExpr], b: Seq[AliasableExpr]) => a ++ b

}
