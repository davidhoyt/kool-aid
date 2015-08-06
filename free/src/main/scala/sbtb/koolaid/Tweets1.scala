package sbtb.koolaid

import sbtb.koolaid.twitter.client._

object Tweets1 extends JvmApp {
  prompt("Twitter handle(s):", "odersky")
  val handles = readprompt("odersky").split(',').map(_.trim).toSeq

  val tweets = tweetsFor(BatchGetTweets(handles:_*))
  printTweets(tweets)
}
