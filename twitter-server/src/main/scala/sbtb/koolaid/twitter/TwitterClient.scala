package sbtb.koolaid.twitter

import twitter4j.auth.AccessToken
import twitter4j.conf.ConfigurationBuilder
import twitter4j.{TwitterFactory, Twitter}

object TwitterClient {
  import net.ceedubs.ficus.Ficus._
  import com.typesafe.config._

  val config = ConfigFactory.load()
  val appKey = config.as[String]("twitter.consumer-key")
  val appSecret = config.as[String]("twitter.consumer-secret")
  val accessToken = config.as[String]("twitter.access-token")
  val accessTokenSecret = config.as[String]("twitter.access-token-secret")

  def apply(): Twitter = {
    val factory = new TwitterFactory(new ConfigurationBuilder().build())
    val t = factory.getInstance()
    t.setOAuthConsumer(appKey, appSecret)
    t.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret))
    t
  }
}
