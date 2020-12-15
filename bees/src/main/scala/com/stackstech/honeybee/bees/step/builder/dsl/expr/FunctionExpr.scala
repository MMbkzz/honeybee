
package com.stackstech.honeybee.bees.step.builder.dsl.expr

case class FunctionExpr(
    functionName: String,
    args: Seq[Expr],
    extraConditionOpt: Option[ExtraConditionExpr],
    aliasOpt: Option[String])
    extends Expr
    with AliasableExpr {

  addChildren(args)

  def desc: String = {
    extraConditionOpt match {
      case Some(cdtn) => s"$functionName(${cdtn.desc} ${args.map(_.desc).mkString(", ")})"
      case _ => s"$functionName(${args.map(_.desc).mkString(", ")})"
    }
  }
  def coalesceDesc: String = desc
  def alias: Option[String] = {
    if (aliasOpt.isEmpty) {
      Some(functionName)
    } else aliasOpt
  }

  override def map(func: Expr => Expr): FunctionExpr = {
    FunctionExpr(
      functionName,
      args.map(func(_)),
      extraConditionOpt.map(func(_).asInstanceOf[ExtraConditionExpr]),
      aliasOpt)
  }
}
