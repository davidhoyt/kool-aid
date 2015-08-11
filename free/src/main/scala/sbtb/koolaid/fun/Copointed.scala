package sbtb.koolaid.fun

import sbtb.koolaid.fun.ops.CopointedOps
import sbtb.koolaid.fun.std.CopointedInstances

trait Copointed[M[_]] {
  def copoint[A](m: M[A]): A
}

object Copointed extends CopointedOps with CopointedInstances
