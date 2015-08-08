package sbtb.koolaid.fun.ops

import sbtb.koolaid.fun.Monoid

trait MonoidOps {

  def apply[A : Monoid] = implicitly[Monoid[A]]

}
