package sbtb.koolaid.fun

import sbtb.koolaid.fun.std.MonoidInstances
import sbtb.koolaid.fun.ops.MonoidOps

trait Monoid[A] {
  def zero: A
  def append(a1: A, a2: => A): A
}

object Monoid extends MonoidOps with MonoidInstances
