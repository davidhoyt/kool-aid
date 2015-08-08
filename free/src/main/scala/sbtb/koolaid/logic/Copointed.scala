package sbtb.koolaid.logic

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

trait Copointed[M[_]] {
  def copoint[A](m: M[A]): A
}

object Copointed {
  implicit object id extends Copointed[Id] {
    override def copoint[A](m: Id[A]): A = m
  }

  implicit object function0 extends Copointed[Function0] {
    override def copoint[A](f: () => A): A = f()
  }

  implicit def future[A](implicit duration: Duration): Copointed[Future] = new Copointed[Future] {
    override def copoint[A](future: Future[A]): A = Await.result(future, duration)
  }
}
