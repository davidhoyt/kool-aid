package sbtb.koolaid.miscellaneous

import sbtb.koolaid.twitter.client.JvmApp

object Arithmetic extends JvmApp {
  sealed trait Expr
  case class Const(i: Int) extends Expr
  case class Add(operand1: Expr, operand2: Expr) extends Expr
  case class Subtract(operand1: Expr, operand2: Expr) extends Expr

  implicit def intToExpr(i: Int): Expr = Const(i)

  def evaluate(expression: Expr): Int = expression match {
    case Const(i) => i
    case Add(o1, o2) => evaluate(o1) + evaluate(o2)
    case Subtract(o1, o2) => evaluate(o1) - evaluate(o2)
  }

  def toString(expression: Expr): String = expression match {
    case Const(i) => i.toString
    case Add(o1, o2) => "(" + toString(o1) + " + " + toString(o2) + ")"
    case Subtract(o1, o2) => "(" + toString(o1) + " - " + toString(o2) + ")"
  }

  val expression = Add(1, 2)

  assert(Add(Const(1), Const(2)) == expression)

  println(evaluate(expression))
  println(toString(Add(3, Add(4, 5))))
  println(toString(Add(Subtract(Add(1, 2), Add(3, 4)), Subtract(Add(5, 6), Add(7, 8)))))
  assert(1 == evaluate(Const(1)))
  assert((3 + (4 - 5)) == evaluate(Add(3, Subtract(4, 5))))
  assert((((1 + 2) - (3 + 4)) + ((5 + 6) - (7 + 8))) == evaluate(Add(Subtract(Add(1, 2), Add(3, 4)), Subtract(Add(5, 6), Add(7, 8)))))
}
