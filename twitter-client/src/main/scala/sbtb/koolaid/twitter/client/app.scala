package sbtb.koolaid.twitter.client

import akka.actor.ActorSystem

import scala.concurrent.{Future, Await}
import scala.concurrent.duration._
import scala.io.StdIn
import scala.scalajs.concurrent.JSExecutionContext

trait JvmApp extends App {
  implicit val system = ActorSystem()
  implicit val executionContext = scala.concurrent.ExecutionContext.global
  implicit val clientContext = ClientContext.jvm

  override def main(args: Array[String]) = {
    super.main(args)
    system.shutdown()
    system.awaitTermination()
  }

  def prompt(prompt: String, default: String = ""): Unit = {
    print(prompt)
    if (default.nonEmpty)
      print(s" [$default]")
    println()
  }

  def readPrompt(default: String = ""): String = {
    val line = StdIn.readLine().trim
    if (line.nonEmpty)
      line
    else
      default
  }

  def tweetsFor(batch: BatchGetTweets)(implicit context: ClientContext): Future[Seq[Tweets]] =
    Request.tweets(batch)(context)

  def awaitTweetsFor(batch: BatchGetTweets)(implicit context: ClientContext): Seq[Tweets] =
    Await.result(Request.tweets(batch)(context), 1.minute)

  def printTweets(tweets: Future[Seq[Tweets]])(implicit context: ClientContext): Future[Unit] =
    tweets map printTweets

  def printTweets(tweets: Seq[Tweets]): Unit =
    for {
      curr <- tweets
      tweet <- curr.tweets
    } {
      printTweet(tweet)
      println()
    }

  def printTweet(tweet: Tweet): Unit = {
    import math._
    val width = 60
    def border() = println(s"+" + ("-" * (width - 2)) + "+")
    def blankLine() = println(withEllipsis("| ", "", " |"))
    def entry(value: String) = println(withEllipsis("| ", value, " |"))
    def entryWrapped(value: String) = println(wrap("| ", value, " |"))
    def withoutEllipsis(marginLeft: String, value: String, marginRight: String): String = {
      val availableWidth = width - marginLeft.length - marginRight.length
      val fill = max(0, availableWidth - value.length)
      marginLeft + value + (" " * fill) + marginRight
    }
    def withEllipsis(marginLeft: String, value: String, marginRight: String): String = {
      val availableWidth = width - marginLeft.length - marginRight.length
      val remainder = availableWidth - value.length

      val modified =
        if (remainder < 0) {
          val amountToStrip = abs(remainder) + "...".length
          value.dropRight(amountToStrip) + "..."
        } else {
          value
        }

      withoutEllipsis(marginLeft, modified, marginRight)
    }

    def wrap(marginLeft: String, value: String, marginRight: String): String = {
      val maxLength = width - marginLeft.length - marginRight.length
      val (wrapped, wrappedLine, _) = value.split(" ").foldLeft ((Seq.empty[String], "", 0)) {
        case (acc @ (seq, line, soFar), nextWord) =>
          if (nextWord equals "") {
            acc
          } else if ((soFar + " ".length + nextWord.length) < maxLength) {
            (seq, line + " " + nextWord, soFar + " ".length + nextWord.length)
          } else {
            (seq :+ line.trim, nextWord, nextWord.length)
          }
      }
      val result = wrapped :+ wrappedLine.trim

      result map (withoutEllipsis(marginLeft, _, marginRight)) mkString "\n"
    }

    border()
    entry(s"${tweet.user.name} (@${tweet.user.screenName})")
    blankLine()
    entryWrapped(tweet.message)
    border()
  }
}

trait ScalaJsApp extends App {
  implicit val executionContext = JSExecutionContext.runNow
  implicit val clientContext = ClientContext.js

  import org.scalajs.dom
  import org.scalajs.dom._
  import scala.collection.mutable
  import scalatags.JsDom.all.{html => _, _}
  
  def inputValueFor(id: String, default: String): String = {
    inputValueFor(id).getOrElse(default)
  }

  def inputValueFor(id: String): Option[String] = {
    val maybeElement = Option(dom.document.getElementById(id))
    val maybeValue = maybeElement.map(_.asInstanceOf[html.Input].value).filter(_.nonEmpty)
    maybeValue
  }

  def swapBuffers(target: html.Div, buffer: html.Div): Unit = {
    target.innerHTML = ""
    target.appendChild(buffer)
  }

  def readPrompt(index: Int, default: String = ""): String =
    inputValueFor(s"text$index", default)
  
  def renderPrompt(list: mutable.Buffer[Node], prompt: String, default: String = ""): Unit = {
    list += div(
      `class` := "label",
      prompt
    ).render
  }

  def renderReadPrompt(list: mutable.Buffer[Node], default: String = ""): Unit = {
    val fieldId = s"text${list.size}"
    list += input(
      `type` := "text",
      id := fieldId,
      value := inputValueFor(fieldId, default = default)
    ).render
  }

  def renderTweet(tweet:Tweet) =
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

  def tweetsFor(batch: BatchGetTweets)(implicit context: ClientContext): Future[Seq[Tweets]] =
    Request.tweets(batch)(context)

  def renderTweets(buffer: html.Div, futureTweets: Future[Seq[Tweets]]): Future[Unit] =
    futureTweets map { tweets =>
      tweets foreach { entry =>
        entry.tweets.take(2) foreach { tweet =>
          buffer.appendChild(renderTweet(tweet).render)
        }
      }
    }
}
