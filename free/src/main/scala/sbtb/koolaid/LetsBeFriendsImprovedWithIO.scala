package sbtb.koolaid

//Cannot read a line in
object LetsBeFriendsImprovedWithIO extends App {
  trait IO { self =>
    def run: Unit

    def map(next: () => IO): IO = new IO {
      override def run: Unit = {
        self.run
        next()
      }
    }

    def flatMap(next: IO => IO): IO = new IO {
      override def run: Unit = {
        self.run
        next(self).run
      }
    }
  }

  def printLine(text: String): IO = new IO {
    override def run: Unit = println(text)
  }

  def requestInput(): IO = ???

  case class Friend(firstName: String) {
    def greet(): IO =
      printLine(s"Hi, I'm $firstName")

    def askName(): String = {
      println("What's your name?")
      val friendsName = readLine()
      friendsName
    }

    def makeFriends(): IO = {
      greet() flatMap { _ =>
        val friendsName = askName()
        printLine(s"Nice to meet you $friendsName")
      }
    }
  }

  Friend("David").makeFriends()
  Friend("David").makeFriends().run
}
