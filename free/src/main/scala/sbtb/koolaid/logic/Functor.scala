package sbtb.koolaid.logic

import scala.concurrent.{Future, ExecutionContext}

trait Functor[F[_]] {
  def map[A, B](fa: F[A])(fn: A => B): F[B]
}

object Functor {

  def apply[F[_]: Functor] = implicitly[Functor[F]]

  implicit object id extends Functor[Id] {
    override def map[A, B](fa: Id[A])(fn: A => B): Id[B] =
      fn(fa)
  }

  implicit object function0 extends Functor[Function0] {
    override def map[A, B](fa: () => A)(fn: A => B): () => B =
      () => fn(fa())
  }

  implicit def future(implicit executionContext: ExecutionContext): Functor[Future] = new Functor[Future] {
    override def map[A, B](fa: Future[A])(fn: A => B): Future[B] =
      fa map fn
  }
}