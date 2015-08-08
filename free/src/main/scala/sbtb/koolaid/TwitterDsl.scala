package sbtb.koolaid

import sbtb.koolaid.fun.free._
import sbtb.koolaid.twitter.client._

import scala.concurrent.Future

object TwitterDsl {

  sealed trait Instruction[A]

  case class Tell(prompt: String, default: String) extends Instruction[Unit]
  case class Ask(default: String) extends Instruction[String]
  case class GetTweets(screenNames: Seq[String]) extends Instruction[Future[Seq[Tweets]]]
  case class DisplayTweets(tweets: Future[Seq[Tweets]]) extends Instruction[Future[Unit]]

  def tell(text: String, default: String = ""): Free[Instruction, Unit] = Suspend(Tell(text, default))
  def ask(default: String): Free[Instruction, String] = Suspend(Ask(default))

  def getTweets(screenNames: Seq[String]): Free[Instruction, Future[Seq[Tweets]]] = Suspend(GetTweets(screenNames))
  def displayTweets(tweets: Future[Seq[Tweets]]): Free[Instruction, Future[Unit]] = Suspend(DisplayTweets(tweets))

}
