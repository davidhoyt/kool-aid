package sbtb.koolaid

import sbtb.koolaid.twitter.client.JvmApp

object Arithmetic extends JvmApp {
  sealed abstract class Expr[N : Numeric]
  case class Const[N : Numeric](i: N) extends Expr[N]
  case class Add[N : Numeric](op1: Expr[N], op2: Expr[N]) extends Expr[N]
  case class Subtract[N : Numeric](op1: Expr[N], op2: Expr[N]) extends Expr[N]

  implicit def numericToExpr[N : Numeric](n: N): Expr[N] = Const(n)

  val expression = Add(1, 2)

  assert(Add(Const(1), Const(2)) == expression)

  def evaluate[N : Numeric](expr: Expr[N]): N = expr match {
    case Const(i) => i
    case Add(op1, op2) => implicitly[Numeric[N]].plus(evaluate(op1), evaluate(op2))
    case Subtract(op1, op2) => implicitly[Numeric[N]].minus(evaluate(op1), evaluate(op2))
  }

  println(evaluate(expression))

  def toString[N : Numeric](expr: Expr[N]): String = expr match {
    case Const(i) => i.toString
    case Add(op1, op2) => s"(${toString(op1)} + ${toString(op2)})"
    case Subtract(op1, op2) => s"(${toString(op1)} - ${toString(op2)})"
  }

  println(toString(Add(3.1, Add(4.2, 5.3))))
  println(toString(Add(Subtract(Add(1, 2), Add(3, 4)), Subtract(Add(5, 6), Add(7, 8)))))
  assert(1 == evaluate(Const(1)))
  assert((3 + (4 - 5)) == evaluate(Add(3, Subtract(4, 5))))
  assert((((1 + 2) - (3 + 4)) + ((5 + 6) - (7 + 8))) == evaluate(Add(Subtract(Add(1, 2), Add(3, 4)), Subtract(Add(5, 6), Add(7, 8)))))
}
