package sbtb.koolaid.fun

import sbtb.koolaid.fun.ops.FunctorOps
import sbtb.koolaid.fun.std.FunctorInstances

trait Functor[F[_]] {
  def map[A, B](fa: F[A])(fn: A => B): F[B]
}

object Functor extends FunctorOps with FunctorInstances
