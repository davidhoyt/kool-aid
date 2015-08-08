package sbtb.koolaid.sjs

import org.scalajs.dom._
import sbtb.koolaid._
import sbtb.koolaid.logic.free._
import sbtb.koolaid.twitter.client._

import scala.collection.mutable
import scala.concurrent.Future
import scala.scalajs.js.annotation.JSExport
import scalatags.JsDom.all.{html => _, _}

// http://localhost:23456/scala-js/target/scala-2.11/classes/twitter.html

@JSExport
object Twitter extends ScalaJsApp {
  import TwitterDsl._

  case class ScalaJsRenderer(list: mutable.Buffer[Node]) extends Evaluator[Instruction] {
    override def evaluate[A](given: Instruction[A]): A = given match {
      case Tell(text, default) => renderPrompt(list, text, default)
      case Ask(default) => renderReadPrompt(list, default) ; default
      case GetTweets(screenNames) => Future(Seq())
      case DisplayTweets(tweets) => Future(())
    }
  }

  case class ScalaJsEvaluator(buffer: html.Div) extends Evaluator[Instruction] {
    var count = -1
    override def evaluate[A](given: Instruction[A]): A = {
      count += 1
      given match {
        case Tell(text, default) => ()
        case Ask(default) => readPrompt(count, default)
        case GetTweets(screenNames) => tweetsFor(BatchGetTweets(screenNames:_*))(clientContext)
        case DisplayTweets(tweets) => renderTweets(buffer, tweets)
      }
    }
  }

  @JSExport
  def main(content: html.Div, result: html.Div): Unit = {

    def render(): Unit = {
      val nodeBuffer = mutable.ListBuffer.empty[Node]
      runFree(Programs.twitter)(ScalaJsRenderer(nodeBuffer))

      val buffer = div(
        div(nodeBuffer),
        renderButton()
      ).render

      swapBuffers(content, buffer)
    }

    def fetchTweets(): Unit = {
      val buffer = div().render
      val monthly = runFree(Programs.twitter)(ScalaJsEvaluator(buffer))
      swapBuffers(result, buffer)
    }

    def renderButton() = {
      val btn = button("Fetch tweets").render
      btn.onclick = (_: MouseEvent) => fetchTweets()
      btn
    }

    render()
  }
}
