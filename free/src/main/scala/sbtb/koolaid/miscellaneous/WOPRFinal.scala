package sbtb.koolaid.miscellaneous

import sbtb.koolaid._
import sbtb.koolaid.fun.free._
import sbtb.koolaid.twitter.client.JvmApp

object WOPRFinal extends JvmApp {
  import InteractsDsl._

  object StandardEvaluator extends Evaluator[Interacts] {
    override def evaluate[A](given: Interacts[A]): A = given match {
      case Tell(text, default) => prompt(text, default)
      case Ask(default) => readPrompt(default)
    }
  }

  runFree(Programs.wopr)(StandardEvaluator)
}
