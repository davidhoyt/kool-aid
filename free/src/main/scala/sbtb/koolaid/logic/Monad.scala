package sbtb.koolaid.logic

trait Monad[M[_]] extends Functor[M] {
  def pure[A](given: A): M[A]
  def flatMap[A, B](given: M[A])(fn: A => M[B]): M[B]
}

object Monad {
  import scala.concurrent.{ExecutionContext, Future}

  def apply[M[_] : Monad] = implicitly[Monad[M]]

  implicit object id extends Monad[Id] {
    override def pure[A](given: A): Id[A] =
      given

    override def map[A, B](given: Id[A])(fn: A => B): Id[B] =
      fn(given)

    override def flatMap[A, B](given: Id[A])(fn: A => Id[B]): Id[B] =
      fn(given)
  }

  implicit object function0 extends Monad[Function0] {
    override def pure[A](given: A): () => A =
      () => given

    override def map[A, B](given: () => A)(fn: A => B): () => B =
      () => fn(given())

    override def flatMap[A, B](given: () => A)(fn: A => () => B): () => B =
      fn(given())
  }

  implicit def future(implicit ec: ExecutionContext): Monad[Future] = new Monad[Future] {
    override def pure[A](given: A): Future[A] =
      Future.successful(given)

    override def map[A, B](given: Future[A])(fn: A => B): Future[B] =
      given map fn

    override def flatMap[A, B](given: Future[A])(fn: A => Future[B]): Future[B] =
      given flatMap fn
  }

  import sbtb.koolaid.logic.free._

  def freeMonad[F[_]]: Monad[({type f[a] = Free[F,a]})#f] = new Monad[({type f[a] = Free[F,a]})#f] {
    override def pure[A](given: A): Free[F, A] = Return[F, A](given)

    override def map[A, B](given: Free[F, A])(fn: (A) => B): Free[F, B] =
      flatMap(given)(a => Return(fn(a)))

    override def flatMap[A, B](given: Free[F, A])(fn: (A) => Free[F, B]): Free[F, B] =
      FlatMap(given, fn)
  }
}