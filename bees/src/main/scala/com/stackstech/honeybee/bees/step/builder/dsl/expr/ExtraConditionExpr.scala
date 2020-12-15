
package com.stackstech.honeybee.bees.step.builder.dsl.expr

case class ExtraConditionExpr(cdtn: String) extends Expr {

  def desc: String = cdtn.toUpperCase

  def coalesceDesc: String = desc

}
