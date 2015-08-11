package sbtb.koolaid.miscellaneous

import sbtb.koolaid.twitter.client._

object Twitter1 extends JvmApp {
  prompt("Twitter handle(s):", "odersky")
  val handles = readPrompt("odersky").split(',').map(_.trim).toSeq

  val tweets = awaitTweetsFor(BatchGetTweets(handles:_*))
  printTweets(tweets)
}
