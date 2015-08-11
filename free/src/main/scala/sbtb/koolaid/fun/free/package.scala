package sbtb.koolaid.fun

import scala.concurrent.{Future, Await, ExecutionContext}
import scala.concurrent.duration.Duration

package object free {
  def runFree[F[_], G[_], A](given: Free[F, A])(nat: F ~> G)(implicit G: Monad[G]): G[A] = ???

  def runFreeAndAwait[F[_], G[_], A](given: Free[F, Future[A]])(nat: F ~> G)(atMost: Duration)(implicit G: Monad[G]): G[A] =
    G.map(runFree(given)(nat))(fa => Await.result(fa, atMost))
}
