package sbtb.koolaid.logic

import scala.concurrent.{Future, Await, ExecutionContext}
import scala.concurrent.duration.Duration

package object free {
  def runFree[F[_], G[_], A](given: Free[F, A])(nat: F ~> G)(implicit G: Monad[G]): G[A] = {
    @annotation.tailrec
    def step(free: Free[F, A]): Free[F, A] = free match {
      case FlatMap(FlatMap(x, f), g) => step(x flatMap (a => f(a) flatMap g))
      case FlatMap(Return(x), f) => step(f(x))
      case _ => free
    }

    step(given) match {
      case Return(a) => G.pure(a)
      case Suspend(fa) => nat(fa)
      case FlatMap(Suspend(fa), f) => G.flatMap(nat(fa))(a => runFree(f(a))(nat))
      case _ => ???
    }
  }

  def runFreeAndAwait[F[_], G[_], A](given: Free[F, Future[A]])(nat: F ~> G)(atMost: Duration)(implicit G: Monad[G]): G[A] =
    G.map(runFree(given)(nat))(fa => Await.result(fa, atMost))
}
