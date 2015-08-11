package sbtb.koolaid.miscellaneous

// co yo nay duh: http://hearnames.com/pronunciations/japanese-names/japanese-surnames/yoneda
// pee ah no: https://www.youtube.com/watch?v=XJfzs89i4CQ

// encodes a natural number as a series of nested data constructors. This is a peano numeral.
// please note that this can be expressed at the type level and i would be happy to point you at sources
// that discuss a type level implementation more deeply than i can do so in this forum.

object Peano extends App {
  sealed trait Nat
  case object Zero extends Nat
  case class Succ(prev: Nat) extends Nat

  def toInt(n: Nat): Int = {
    def step(soFar: Int, n: Nat): Int = n match {
      case Zero => soFar
      case Succ(next) => step(soFar + 1, next)
    }

    step(0, n)
  }

  def fromInt(number: Int): Nat = {
    def step(soFar: Int, n: Nat): Nat = soFar match {
      case _ if soFar < number => step(soFar + 1, Succ(n))
      case _ => n
    }

    require(number >= 0)
    step(0, Zero)
  }

  val _0 = Zero
  val _1 = Succ(Zero)
  val _2 = Succ(Succ(Zero))
  val _3 = Succ(Succ(Succ(Zero)))

  assert(_0 == fromInt(0))
  assert(_1 == fromInt(1))
  assert(_2 == fromInt(2))
  assert(_3 == fromInt(3))

  assert(0 == toInt(_0))
  assert(1 == toInt(_1))
  assert(2 == toInt(_2))
  assert(3 == toInt(_3))

  println(fromInt(6))
}
