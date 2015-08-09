package sbtb.koolaid

object Programs {
  val wopr = {
    import InteractsDsl._
    for {
         _ <- tell("PLEASE ENTER NAME:", "Professor Falken")
      name <- ask("Professor Falken")
         _ <- tell(s"GREETINGS ${name.toUpperCase}. SHALL WE PLAY A GAME?")
    } yield ()
  }

  val twitter = {
    import TwitterDsl._
    for {
            _ <- tell("Twitter handle(s):", "tpolecat")
      handles <- ask("tpolecat")
       tweets <- getTweets(handles.split(','))
         done <- displayTweets(tweets)
    } yield done
  }
}
