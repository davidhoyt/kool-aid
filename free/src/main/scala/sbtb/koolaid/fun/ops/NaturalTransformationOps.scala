package sbtb.koolaid.fun.ops

import sbtb.koolaid.fun._

trait NaturalTransformationOps {

   implicit def identity[F[_]] = new (F ~> F) {
     override def apply[A](given: F[A]): F[A] = given
   }

 }
