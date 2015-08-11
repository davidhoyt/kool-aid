package sbtb.koolaid.fun.std

import sbtb.koolaid.fun.Monoid

trait MonoidInstances {

  implicit object integerAddition extends Monoid[Int] {
    override val zero: Int = 0

    override def append(a1: Int, a2: => Int): Int = a1 + a2
  }

  implicit object stringConcatenation extends Monoid[String] {
    override val zero: String = ""

    override def append(a1: String, a2: => String): String = a1 + a2
  }
}
