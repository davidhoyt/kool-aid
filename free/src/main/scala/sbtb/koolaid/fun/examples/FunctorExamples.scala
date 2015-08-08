package sbtb.koolaid.fun.examples

import sbtb.koolaid.fun._

object FunctorExamples extends App {
  val begin = Option(1)
  val increment = (x: Int) => x + 1

  assert(Some(2) == (begin map increment))
  assert(Some(2) == Functor[Option].map(begin)(increment))
}
