

trait Monad[F[_]] {
  def unit[A](value: A): F[A]
  def bind[A, B](given: F[A])(fn: A => F[B]): F[B]
  def map[A, B](given: F[A])(fn: A => B): F[B] = bind(given)(fn andThen unit)
}

object Monad {
  def apply[F[_] : Monad]: Monad[F] = implicitly[Monad[F]]
}

