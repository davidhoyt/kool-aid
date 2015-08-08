package sbtb.koolaid

object Programs {
  import TwitterDsl._

  val twitter =
    for {
            _ <- tell("Twitter handle(s):", "odersky")
      handles <- ask("odersky")
       tweets <- getTweets(handles.split(','))
         done <- displayTweets(tweets)
    } yield done
}
