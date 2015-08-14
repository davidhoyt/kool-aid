package sbtb.koolaid

import sbtb.koolaid.fun.free._

object InteractsDsl {

  sealed trait Interacts[A]

  case class Tell(prompt: String, default: String) extends Interacts[Unit]
  case class Ask(default: String) extends Interacts[String]

  def tell(text: String, default: String = ""): Free[Interacts, Unit] = Suspend(Tell(text, default))
  def ask(default: String): Free[Interacts, String] = Suspend(Ask(default))

}
