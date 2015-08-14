package sbtb.koolaid

import sbtb.koolaid.fun._
import sbtb.koolaid.fun.free._
import sbtb.koolaid.twitter.client._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object Twitter extends JvmApp {
  import TwitterDsl._

  object StandardEvaluator extends (Instruction ~> Id) {
    override def apply[A](given: Instruction[A]): Id[A] = given match {
      case Tell(text, default) => prompt(text, default)
      case Ask(default) => readPrompt(default)
      case GetTweets(screenNames) => tweetsFor(BatchGetTweets(screenNames:_*))
      case DisplayTweets(tweets) => printTweets(tweets)
    }
  }

  case class TestEvaluator(answers: String*) extends (Instruction ~> Id) {
    override def apply[A](given: Instruction[A]): Id[A] = {
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

  case class FlightRecorder[F[_]](interpreter: (F ~> Id))(atMost: Duration) extends (F ~> Id) {
    var record = List.empty[Any]

    override def apply[A](given: F[A]): A = {
      val result = interpreter(given)
      val recordResult = result match {
        case f: Future[_] => Await.result(f, atMost)
        case x => x
      }
      record = record :+ recordResult
      result
    }
  }

  runFreeAndAwait(Programs.twitter)(StandardEvaluator)(1.minute)
  //runFreeAndAwait(Programs.twitter)(TestEvaluator("test"))(1.minute)

  //val recorder = FlightRecorder(StandardEvaluator)(1.minute)
  ////val recorder = FlightRecorder(TestEvaluator("test"))(1.minute)
  //runFreeAndAwait(Programs.twitter)(recorder)(1.minute)
  //println(s"Results: ${recorder.record}")
}
