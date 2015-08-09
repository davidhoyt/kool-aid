package sbtb.koolaid

package object fun {
  type ~>[F[_], G[_]] = NaturalTransformation[F, G]
  type Id[A] = A
  val Id = Monad[Id]
}
