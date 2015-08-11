package sbtb.koolaid.fun

import sbtb.koolaid.fun.ops.MonadOps
import sbtb.koolaid.fun.std.MonadInstances

trait Monad[M[_]] extends Functor[M] {
  def pure[A](given: A): M[A]
  def flatMap[A, B](given: M[A])(fn: A => M[B]): M[B]

  override def map[A, B](given: M[A])(fn: (A) => B): M[B] =
    flatMap(given)(a => pure(fn(a)))
}

object Monad extends MonadOps with MonadInstances
