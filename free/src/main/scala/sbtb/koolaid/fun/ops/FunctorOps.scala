package sbtb.koolaid.fun.ops

import sbtb.koolaid.fun._

trait FunctorOps {

  def apply[F[_]: Functor] = implicitly[Functor[F]]
  
}
