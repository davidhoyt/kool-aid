package sbtb.koolaid.fun.examples

import sbtb.koolaid.fun._

object MonoidExamples extends App {
  val additionMonoid = Monoid.integerAddition
  assert(2 == additionMonoid.append(additionMonoid.zero, 2))
  assert(5 == additionMonoid.append(2, 3))

  val stringMonoid = Monoid.stringConcatenation
  assert("" + "SBTB" == stringMonoid.append(stringMonoid.zero, "SBTB"))
  assert("Scala " + "By the Bay" == stringMonoid.append("Scala ", "By the Bay"))
}
