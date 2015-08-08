package sbtb.koolaid.logic.free

import sbtb.koolaid.logic._

import scala.concurrent.Future

trait Evaluator[F[_]] extends Copointed[F] { self =>
  def evaluate[A](given: F[A]): A

  final override def copoint[A](m: F[A]): A = evaluate(m)

  def ~>[G[_] : Monad]: (F ~> G) =
    Evaluator.nat(self, Monad[G])
}

object Evaluator {
  def apply[F[_] : Evaluator] = implicitly[Evaluator[F]]

  implicit def identity[F[_], E <: Evaluator[F]](E: E): (F ~> Id) = new (F ~> Id) {
    override def apply[A](given: F[A]): Id[A] = Monad.id.pure(E.evaluate(given))
  }

  implicit def nat[F[_], G[_], E <: Evaluator[F]](implicit E: E, G: Monad[G]) = new (F ~> G) {
    override def apply[A](given: F[A]): G[A] = G.pure(E.evaluate(given))
  }
}
