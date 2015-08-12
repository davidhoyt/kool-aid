package sbtb.koolaid

import sbtb.koolaid.fun._
import sbtb.koolaid.fun.free._
import sbtb.koolaid.twitter.client._

object WOPR extends JvmApp {

  object Interacts {
    sealed trait Interacts[A]
    implicit object interactsFunctor extends Functor[Interacts] {
      override def map[A, B](fa: Interacts[A])(fn: A => B): Interacts[B] = ???
    }

    case class Tell(text: String, default: String) extends Interacts[Unit]
    case class Ask(default: String) extends Interacts[String]

    def tell(text: String, default: String = ""): Free[Interacts, Unit] = ???
    def ask(default: String): Free[Interacts, String] = ???
  }

  import Interacts._

  val program: Free[Interacts, Unit] =
    for {
         _ <- tell("PLEASE ENTER NAME:", "Professor Falken")
      name <- ask("Professor Falken")
         _ <- tell(s"GREETINGS ${name.toUpperCase}. SHALL WE PLAY A GAME?")
    } yield ()

  runNaive(program)
}
