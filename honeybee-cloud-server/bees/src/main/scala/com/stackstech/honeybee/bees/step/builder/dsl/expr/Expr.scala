
package com.stackstech.honeybee.bees.step.builder.dsl.expr

/**
 * expr parsed by griffin dsl
 */
trait Expr extends TreeNode with ExprTag with Serializable {

  def desc: String

  def coalesceDesc: String

  def extractSelf: Expr = this

  // execution
  def map(func: Expr => Expr): Expr = func(this)

}
