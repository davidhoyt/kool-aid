package sbtb.koolaid.fun.ops

import sbtb.koolaid.fun._
import sbtb.koolaid.fun.free._

trait MonadOps {

  def apply[M[_] : Monad] = implicitly[Monad[M]]

  def freeMonad[F[_]]: Monad[({type f[a] = Free[F,a]})#f] = new Monad[({type f[a] = Free[F,a]})#f] {
    override def pure[A](given: A): Free[F, A] = Return[F, A](given)

    override def map[A, B](given: Free[F, A])(fn: (A) => B): Free[F, B] =
      flatMap(given)(a => Return(fn(a)))

    override def flatMap[A, B](given: Free[F, A])(fn: (A) => Free[F, B]): Free[F, B] =
      FlatMap(given, fn)
  }
}
