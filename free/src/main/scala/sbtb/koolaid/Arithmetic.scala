package sbtb.koolaid

import sbtb.koolaid.twitter.client.JvmApp

object Arithmetic extends JvmApp {
  sealed trait Expr
  case class Const(i: Int) extends Expr
  case class Add(op1: Expr, op2: Expr) extends Expr
  case class Subtract(op1: Expr, op2: Expr) extends Expr

  implicit def intToExpr(i: Int): Expr = Const(i)

  val expression = Add(1, 2)

  assert(Add(Const(1), Const(2)) == expression)

  def evaluate(expr: Expr): Int = expr match {
    case Const(i) => i
    case Add(op1, op2) => evaluate(op1) + evaluate(op2)
    case Subtract(op1, op2) => evaluate(op1) - evaluate(op2)
  }

  println(evaluate(expression))

  def toString(expr: Expr): String = expr match {
    case Const(i) => i.toString
    case Add(op1, op2) => s"(${toString(op1)} + ${toString(op2)})"
    case Subtract(op1, op2) => s"(${toString(op1)} - ${toString(op2)})"
  }

  println(toString(Add(3, Add(4, 5))))
  println(toString(Add(Subtract(Add(1, 2), Add(3, 4)), Subtract(Add(5, 6), Add(7, 8)))))
  assert(1 == evaluate(Const(1)))
  assert((3 + (4 - 5)) == evaluate(Add(3, Subtract(4, 5))))
  assert((((1 + 2) - (3 + 4)) + ((5 + 6) - (7 + 8))) == evaluate(Add(Subtract(Add(1, 2), Add(3, 4)), Subtract(Add(5, 6), Add(7, 8)))))
}
