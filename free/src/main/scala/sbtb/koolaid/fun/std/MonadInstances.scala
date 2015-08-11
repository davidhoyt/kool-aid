package sbtb.koolaid.fun.std

import sbtb.koolaid.fun._

import scala.concurrent.{ExecutionContext, Future}

trait MonadInstances {

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

}
