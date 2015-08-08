package sbtb.koolaid.logic

trait Natural[F[_], G[_]] {
  def apply[A](given: F[A]): G[A]
}

object Natural {
  implicit def identity[F[_]] = new (F ~> F) {
    override def apply[A](given: F[A]): F[A] = given
  }
}
