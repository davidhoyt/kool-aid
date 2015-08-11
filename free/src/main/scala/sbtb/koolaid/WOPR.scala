package sbtb.koolaid

import sbtb.koolaid.twitter.client._

object WOPR extends JvmApp {

   prompt("PLEASE ENTER NAME:", "Professor Falken")
   val name = readPrompt("Professor Falken")
   prompt(s"GREETINGS ${name.toUpperCase}. SHALL WE PLAY A GAME?")

 }
