package sbtb.koolaid

import sbtb.koolaid.twitter.client._

import scala.concurrent.Future

object TwitterStep1 extends JvmApp {
  trait IO[A] { self =>
    def run: A

    def map[B](fn: A => B): IO[B] = new IO[B] {
      def run = fn(self.run)
    }

    def flatMap[B](fn: A => IO[B]) = new IO[B] {
      override def run: B = fn(self.run).run
    }
  }

  sealed trait Interactive[A] extends IO[A]
  case class Ask(text: String, default: String) extends Interactive[Unit] { def run = prompt(text, default) }
  case class Answer(default: String) extends Interactive[String] { def run = readPrompt(default)}

  def ask(text: String, default: String = ""): Interactive[Unit] = Ask(text, default)
  def answer(default: String): Interactive[String] = Answer(default)

  sealed trait Twitter[A] extends IO[A]
  case class GetTweets(screenNames: Seq[String]) extends Twitter[Future[Seq[Tweets]]] { def run = tweetsFor(BatchGetTweets(screenNames:_*)) }
  case class DisplayTweets(tweets: Future[Seq[Tweets]]) extends Twitter[Future[Unit]] { def run = printTweets(tweets) }

  def getTweets(screenNames: Seq[String]): Twitter[Future[Seq[Tweets]]] = GetTweets(screenNames)
  def displayTweets(tweets: Future[Seq[Tweets]]): Twitter[Future[Unit]] = DisplayTweets(tweets)

  val program =
    for {
            _ <- ask("Twitter handle(s):", "odersky")
      handles <- answer("odersky")
       tweets <- getTweets(handles.split(','))
         done <- displayTweets(tweets)
    } yield ()

  //program.run
  program.run
}
