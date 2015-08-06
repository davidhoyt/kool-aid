package sbtb.koolaid.twitter.client

import scala.concurrent.Future

sealed trait Request
case class GetTweets(screenName: String) extends Request
case class BatchGetTweets(batched: List[GetTweets]) extends Request

object BatchGetTweets {
  import scala.language.postfixOps
  def apply(screenNames: String*): BatchGetTweets =
    BatchGetTweets(screenNames map GetTweets toList)
}

object Request {
  implicit def batchToSeq(batch: BatchGetTweets): Seq[String] =
    batch.batched.map(_.screenName)

  def tweets(batch: BatchGetTweets)(implicit client: ClientContext): Future[Seq[Tweets]] = {
    val screenNames = batchToSeq(batch)
    val url = "/tweets/" + screenNames.mkString(",")
    client.get[Seq[Tweets]](url)
  }
}
