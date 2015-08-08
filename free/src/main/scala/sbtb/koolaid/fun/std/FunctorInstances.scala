package sbtb.koolaid.fun.std

import sbtb.koolaid.fun._

import scala.concurrent.{ExecutionContext, Future}

trait FunctorInstances {

  implicit object id extends Functor[Id] {
    override def map[A, B](fa: Id[A])(fn: A => B): Id[B] =
      fn(fa)
  }

  implicit object function0 extends Functor[Function0] {
    override def map[A, B](fa: () => A)(fn: A => B): () => B =
      () => fn(fa())
  }

  implicit object option extends Functor[Option] {
    override def map[A, B](fa: Option[A])(fn: A => B): Option[B] =
      fa map fn
  }

  implicit def future(implicit executionContext: ExecutionContext): Functor[Future] = new Functor[Future] {
    override def map[A, B](fa: Future[A])(fn: A => B): Future[B] =
      fa map fn
  }
}
