
package com.stackstech.honeybee.bees.step.builder.dsl.parser

import com.stackstech.honeybee.bees.configuration.enums.DqType._
import com.stackstech.honeybee.bees.step.builder.dsl.expr._

/**
 * parser for dsl rule
 */
case class GriffinDslParser(dataSourceNames: Seq[String], functionNames: Seq[String])
    extends BasicParser {

  import Operator._

  /**
   * -- profiling clauses --
   * <profiling-clauses> = <select-clause> [ <from-clause> ]+ [ <where-clause> ]+
   *  [ <groupby-clause> ]+ [ <orderby-clause> ]+ [ <limit-clause> ]+
   */
  def profilingClause: Parser[ProfilingClause] =
    selectClause ~ opt(fromClause) ~ opt(whereClause) ~
      opt(groupbyClause) ~ opt(orderbyClause) ~ opt(limitClause) ^^ {
      case sel ~ fromOpt ~ whereOpt ~ groupbyOpt ~ orderbyOpt ~ limitOpt =>
        val preClauses = Seq(whereOpt).flatten
        val postClauses = Seq(orderbyOpt, limitOpt).flatten
        ProfilingClause(sel, fromOpt, groupbyOpt, preClauses, postClauses)
    }

  /**
   * -- uniqueness clauses --
   * <uniqueness-clauses> = <expr> [, <expr>]+
   */
  def uniquenessClause: Parser[UniquenessClause] =
    rep1sep(expression, Operator.COMMA) ^^ (exprs => UniquenessClause(exprs))

  /**
   * -- distinctness clauses --
   * <sqbr-expr> = "[" <expr> "]"
   * <dist-expr> = <sqbr-expr> | <expr>
   * <distinctness-clauses> = <distExpr> [, <distExpr>]+
   */
  def sqbrExpr: Parser[Expr] = LSQBR ~> expression <~ RSQBR ^^ { expr =>
    expr.tag = "[]"; expr
  }
  def distExpr: Parser[Expr] = expression | sqbrExpr
  def distinctnessClause: Parser[DistinctnessClause] =
    rep1sep(distExpr, Operator.COMMA) ^^ (exprs => DistinctnessClause(exprs))

  /**
   * -- timeliness clauses --
   * <timeliness-clauses> = <expr> [, <expr>]+
   */
  def timelinessClause: Parser[TimelinessClause] =
    rep1sep(expression, Operator.COMMA) ^^ (exprs => TimelinessClause(exprs))

  /**
   * -- completeness clauses --
   * <completeness-clauses> = <expr> [, <expr>]+
   */
  def completenessClause: Parser[CompletenessClause] =
    rep1sep(expression, Operator.COMMA) ^^ (exprs => CompletenessClause(exprs))

  def parseRule(rule: String, dqType: DqType): ParseResult[Expr] = {
    val rootExpr = dqType match {
      case Accuracy => logicalExpression
      case Profiling => profilingClause
      case Uniqueness => uniquenessClause
      case Distinct => distinctnessClause
      case Timeliness => timelinessClause
      case Completeness => completenessClause
      case _ => expression
    }
    parseAll(rootExpr, rule)
  }

}
