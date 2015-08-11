package sbtb.koolaid.fun.free

sealed trait Free[F[_], A] { self =>
  def map[B](fn: A => B): Free[F, B] =
    ???

  def flatMap[B](fn: A => Free[F, B]): Free[F, B] =
    ???
}
