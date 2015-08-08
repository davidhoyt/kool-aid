package sbtb.koolaid

import sbtb.koolaid.logic.free._
import sbtb.koolaid.twitter.client._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object TwitterStep2 extends JvmApp {
  import TwitterDsl._
  
  object StandardEvaluator extends Evaluator[Instruction] {
    override def evaluate[A](given: Instruction[A]): A = given match {
      case Tell(text, default) => prompt(text, default)
      case Ask(default) => readPrompt(default)
      case GetTweets(screenNames) => tweetsFor(BatchGetTweets(screenNames:_*))
      case DisplayTweets(tweets) => printTweets(tweets)
    }
  }

  case class TestEvaluator(answers: String*) extends Evaluator[Instruction] {
    override def evaluate[A](given: Instruction[A]): A = {
      var remainingAnswers = answers
      given match {
      case Tell(_, _) => ()
      case Ask(_) =>
        val response = remainingAnswers.head
        remainingAnswers = remainingAnswers.tail
        response
      case GetTweets(_) => Future(Seq(Tweets("test", Seq(Tweet(123L, 0L, "", message = "A test tweet", user = User("test", "Test User", "", ""))))))
      case DisplayTweets(tweets) => printTweets(tweets)
    }}
  }

  runFreeAndAwait(Programs.twitter)(StandardEvaluator)(1.minute)
  //runFreeAndAwait(Programs.twitter)(TestEvaluator("test"))(1.minute)
}
