package sbtb.koolaid.twitter.client

import akka.actor.ActorSystem

import scala.concurrent.Await
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
    if (prompt.nonEmpty)
      print(s" [$default]")
    println()
  }

  def readprompt(default: String = ""): String = {
    val line = StdIn.readLine().trim
    if (line.nonEmpty)
      line
    else
      default
  }

  def tweetsFor(batch: BatchGetTweets)(implicit context: ClientContext): Seq[Tweets] =
    Await.result(Request.tweets(batch)(context), 1.minute)

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
}
