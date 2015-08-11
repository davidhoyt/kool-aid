package sbtb.koolaid.fun

import sbtb.koolaid.fun.ops.NaturalTransformationOps
import sbtb.koolaid.fun.std.NaturalTransformationInstances

trait NaturalTransformation[F[_], G[_]] {
  def apply[A](given: F[A]): G[A]
}

object NaturalTransformation extends NaturalTransformationOps with NaturalTransformationInstances
