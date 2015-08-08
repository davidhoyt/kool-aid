package sbtb.koolaid

import sbtb.koolaid.twitter.client._

object HelloStep1 extends JvmApp {
  prompt("Your name please:", "Conan")
  val name = readPrompt("Conan")
  prompt(s"Hello $name")
}
