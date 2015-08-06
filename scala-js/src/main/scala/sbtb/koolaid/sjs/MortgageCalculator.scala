package sbtb.koolaid.sjs

import org.scalajs.dom
import org.scalajs.dom._
import sbtb.koolaid.sjs.Utils._
import sbtb.koolaid.twitter.client.{BatchGetTweets, ScalaJsApp, Tweet, Request}
import Request._

import scala.collection.mutable
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import scalatags.JsDom.all.{html => _, _}

@JSExport
object MortgageCalculator extends ScalaJsApp {
  import sbtb.koolaid.MortgageCalculator6._

  @JSExport
  def main(content: html.Div, result: html.Div): Unit = {

    def ScalaJsConsoleRenderer(list: mutable.Buffer[Node]) = new Implementation[Console] {
      import Console._

      override def run[A](given: Console[A]): A =
        given match {
          case ReadLine(default, conv) =>
            val fieldId = s"text${list.size}"
            list += input(
              `type` := "text",
                  id := fieldId,
               value := inputValueFor(fieldId, default = default.toString)
            ).render
            None
          case PrintLine(line) =>
            list += div(
              `class` := "label",
              line
            ).render
            ()
        }
    }

    def ScalaJsConsoleRunner = new Implementation[Console] {
      import Console._

      var count = -1
      override def run[A](given: Console[A]): A = {
        count += 1
        given match {
          case ReadLine(default, conv) => inputValueFor(s"text${count}").flatMap(conv)
          case PrintLine(_) => ()
        }
      }
    }

    def render(): Unit = {
      val nodeBuffer = mutable.ListBuffer.empty[Node]
      runFree(monthlyPayment())(ScalaJsConsoleRenderer(nodeBuffer) ~> Function0Monad)

      val buffer = div(
        div(nodeBuffer),
        renderButton()
      ).render

      renderTweets(buffer)

      swapBuffers(content, buffer)
    }

    def calculateMonthly(): Unit = {
      val monthly = runFree(monthlyPayment())(ScalaJsConsoleRunner ~> Function0Monad)
      val payment = monthly()
      val buffer =
        div(
          span(s"Monthly payment: "),
          span(
            `class` := "monthly",
            f"$$$payment%.2f"
          )
        ).render
      swapBuffers(result, buffer)
    }

    def renderButton() = {
      val btn = button("Calculate").render
      btn.onclick = (_: MouseEvent) => calculateMonthly()
      btn
    }

    def renderTweet(tweet:Tweet) = {
      div(
        blockquote(
          `class` := "twitter-tweet",
          div(
            `class` := "Tweet-header",
            div(
              `class` := "Tweet-author",
              a(
                `class` := "Tweet-authorLink",
                href := tweet.user.url,
                span(
                  `class` := "Tweet-authorAvatar",
                  img(
                    `class` := "Avatar",
                    src := tweet.user.profileImageUrl
                  )
                ),
                span(
                  `class` := "Tweet-authorName",
                  tweet.user.name
                ),
                span(
                  `class` := "Tweet-authorScreenName",
                  s"@${tweet.user.screenName}"
                )
              )
            )
          ),
          p(
            a(
              href := s"${tweet.url}",
              tweet.message
            )
          )
        )
      )
    }

    def renderTweets(buffer: html.Div) = {
      Request.tweets(BatchGetTweets("insaan", "WiteCollrScalar", "mpilquist")).foreach { perScreenName =>
        perScreenName foreach { entry =>
          entry.tweets.take(2) foreach { tweet =>
            buffer.appendChild(renderTweet(tweet).render)
          }
        }
      }
    }

    render()
    calculateMonthly()
  }
}
