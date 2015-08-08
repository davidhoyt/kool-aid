package sbtb.koolaid

import sbtb.koolaid.twitter.client.JvmApp

object Arithmetic2 extends JvmApp {
  sealed trait Expr
  case class Const(i: Int) extends Expr
  case class Add(operand1: Expr, operand2: Expr) extends Expr
  case class Subtract(operand1: Expr, operand2: Expr) extends Expr

  val expression = Add(Const(1), Const(2))

  def evaluate(expression: Expr): Int = expression match {
    case Const(i) => i
    case Add(op1, op2) => evaluate(op1) + evaluate(op2)
    case Subtract(op1, op2) => evaluate(op1) - evaluate(op2)
  }

  assert(1 + 2 == evaluate(expression))
  assert(1 + 2 + (4 - 3) == evaluate(Add(Add(Const(1), Const(2)), Subtract(Const(4), Const(3)))))
}
