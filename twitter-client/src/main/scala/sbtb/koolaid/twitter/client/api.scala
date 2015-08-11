package sbtb.koolaid.twitter.client

case class User(screenName: String, name: String, url: String, profileImageUrl: String)
case class Tweet(id: Long, created: Long, url: String, message: String, user: User)
case class Tweets(screenName: String, tweets: Seq[Tweet])
