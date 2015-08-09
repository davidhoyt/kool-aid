package sbtb.koolaid

import sbtb.koolaid.twitter.client.JvmApp

object Arithmetic extends JvmApp {
  

  // implicit def intToExpr(i: Int): Expr = Const(i)
  //
  // val expression = Add(1, 2)
  //
  // assert(Add(Const(1), Const(2)) == expression)
  //
  // println(evaluate(expression))
  // println(toString(Add(3, Add(4, 5))))
  // println(toString(Add(Subtract(Add(1, 2), Add(3, 4)), Subtract(Add(5, 6), Add(7, 8)))))
  // assert(1 == evaluate(Const(1)))
  // assert((3 + (4 - 5)) == evaluate(Add(3, Subtract(4, 5))))
  // assert((((1 + 2) - (3 + 4)) + ((5 + 6) - (7 + 8))) == evaluate(Add(Subtract(Add(1, 2), Add(3, 4)), Subtract(Add(5, 6), Add(7, 8)))))
}
