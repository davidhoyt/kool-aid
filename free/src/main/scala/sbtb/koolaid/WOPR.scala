package sbtb.koolaid

import sbtb.koolaid.fun._
import sbtb.koolaid.fun.free._
import sbtb.koolaid.twitter.client._

object WOPR extends JvmApp {

  object Interacts {
    sealed trait Interacts[A]

    case class Tell(text: String, default: String) extends Interacts[Unit]
    case class Ask(default: String) extends Interacts[String]

    def tell(text: String, default: String = ""): Free[Interacts, Unit] = Suspend(Tell(text, default))
    def ask(default: String): Free[Interacts, String] = Suspend(Ask(default))
  }

  import Interacts._

  val program: Free[Interacts, Unit] =
    for {
         _ <- tell("PLEASE ENTER NAME:", "Professor Falken")
      name <- ask("Professor Falken")
         _ <- tell(s"GREETINGS ${name.toUpperCase}. SHALL WE PLAY A GAME?")
    } yield ()

  object standardIO extends (Interacts ~> Id) {
    override def apply[A](given: Interacts[A]): Id[A] = given match {
      case Tell(text, default) => prompt(text, default)
      case Ask(default) => readPrompt(default)
    }
  }

  def testIO(answers: String*): (Interacts ~> Id) = new (Interacts ~> Id) {
    var remaining = answers

    override def apply[A](given: Interacts[A]): Id[A] = given match {
      case Tell(text, default) =>
        prompt(text, default)

      case Ask(default) =>
        val response = remaining.head
        remaining = remaining.tail
        response
    }
  }

  runFree(program)(standardIO)
  //runFree(program)(testIO("Haskell Curry"))
}
