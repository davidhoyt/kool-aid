package sbtb.koolaid.fun.free

sealed trait Free[F[_], A] { self =>
  def map[B](fn: A => B): Free[F, B] =
    flatMap(a => Return(fn(a)))

  def flatMap[B](fn: A => Free[F, B]): Free[F, B] =
    FlatMap(self, (a: A) => fn(a))
}

case class Return[F[_], A](value: A) extends Free[F, A]
case class Suspend[F[_], A](fa: F[A]) extends Free[F, A]
case class FlatMap[F[_], A, B](s: Free[F, A], f: A => Free[F, B]) extends Free[F, B]
