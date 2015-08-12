package sbtb.koolaid.fun.free

sealed trait Free[F[_], A] { self =>
  def map[B](fn: A => B): Free[F, B] =
    ???

  def flatMap[B](fn: A => Free[F, B]): Free[F, B] =
    ???
}

case class Return[F[_], A](value: A) extends Free[F, A]
case class FlatMap[F[_], A, B](given: Free[F, A], fn: A => Free[F, B]) extends Free[F, B]
