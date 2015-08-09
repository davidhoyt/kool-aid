package sbtb.koolaid.miscellaneous

import sbtb.koolaid.twitter.client._

object WOPR1 extends JvmApp {

  prompt("PLEASE ENTER NAME:", "Professor Falken")
  val name = readPrompt("Professor Falken")
  prompt(s"GREETINGS ${name.toUpperCase}. SHALL WE PLAY A GAME?")

}
