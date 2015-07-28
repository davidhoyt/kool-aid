package sbtb.koolaid

//Can now read a line in!
object LetsBeFriendsImprovedWithIOParameterized extends App {
  trait IO[A] { self =>
    def run: A

    def map[B](next: A => B): IO[B] = new IO[B] {
      override def run: B =
        next(self.run)
    }

    def flatMap[B](next: A => IO[B]): IO[B] = new IO[B] {
      override def run: B =
        next(self.run).run
    }
  }

  def printLine(text: String): IO[Unit] = new IO[Unit] {
    override def run: Unit = println(text)
  }

  def requestInput(): IO[String] = new IO[String] {
    override def run: String = readLine()
  }

  case class Friend(firstName: String) {
    def greet(): IO[Unit] =
      printLine(s"Hi, I'm $firstName")

    def askName(): IO[String] = {
      printLine("What's your name?") flatMap { _ =>
        requestInput()
      }
    }

    def makeFriends(): IO[Unit] = {
      greet() flatMap { _ =>
        askName() flatMap { friendsName =>
          printLine(s"Nice to meet you $friendsName")
        }
      }
    }

    def makeFriendsTakeTwo(): IO[Unit] =
      for {
                  _ <- greet()
        friendsName <- askName()
                  _ <- printLine(s"Nice to meet you $friendsName")
      } yield ()
  }

//  Friend("David").makeFriends().run
  Friend("David").makeFriendsTakeTwo().run
}
