
package com.stackstech.honeybee.bees.step.builder.dsl.expr

trait AliasableExpr extends Expr {

  def alias: Option[String]

}
