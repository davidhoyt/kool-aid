package sbtb.koolaid

package object logic {
  type ~>[F[_], G[_]] = Natural[F, G]
  type Id[A] = A
}
