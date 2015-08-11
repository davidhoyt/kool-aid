package sbtb.koolaid.fun.examples

import sbtb.koolaid.fun._

object NaturalTransformationExamples extends App {
  val optionIdentity = new (Option ~> Option) {
    override def apply[A](given: Option[A]): Option[A] = given
  }

  assert(Some(1) == optionIdentity(Some(1)))

  val optionToList = new (Option ~> List) {
    override def apply[A](given: Option[A]): List[A] = given.toList
  }

  assert(List(1) == optionToList(Some(1)))
}
