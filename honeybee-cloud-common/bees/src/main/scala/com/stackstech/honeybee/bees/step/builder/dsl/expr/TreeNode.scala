
package com.stackstech.honeybee.bees.step.builder.dsl.expr

import scala.reflect.ClassTag

trait TreeNode extends Serializable {

  var children: Seq[TreeNode] = Seq[TreeNode]()

  def addChild(expr: TreeNode): Unit = { children :+= expr }
  def addChildren(exprs: Seq[TreeNode]): Unit = { children ++= exprs }

  def preOrderTraverseDepthFirst[T, A <: TreeNode](z: T)(seqOp: (A, T) => T, combOp: (T, T) => T)(
      implicit tag: ClassTag[A]): T = {

    val clazz = tag.runtimeClass
    if (clazz.isAssignableFrom(this.getClass)) {
      val tv = seqOp(this.asInstanceOf[A], z)
      children.foldLeft(combOp(z, tv)) { (ov, tn) =>
        combOp(ov, tn.preOrderTraverseDepthFirst(z)(seqOp, combOp))
      }
    } else {
      z
    }

  }
  def postOrderTraverseDepthFirst[T, A <: TreeNode](
      z: T)(seqOp: (A, T) => T, combOp: (T, T) => T)(implicit tag: ClassTag[A]): T = {

    val clazz = tag.runtimeClass
    if (clazz.isAssignableFrom(this.getClass)) {
      val cv = children.foldLeft(z) { (ov, tn) =>
        combOp(ov, tn.postOrderTraverseDepthFirst(z)(seqOp, combOp))
      }
      combOp(z, seqOp(this.asInstanceOf[A], cv))
    } else {
      z
    }
  }

}
