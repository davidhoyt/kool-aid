package sbtb.koolaid.fun.std

import sbtb.koolaid.fun._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

trait CopointedInstances {

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
