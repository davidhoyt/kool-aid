package sbtb.koolaid

object LetsBeFriendsImproved extends App {
  case class Friend(firstName: String) {
    def greet(): Unit =
      println(s"Hi, I'm $firstName")

    def askName(): String = {
      println("What's your name?")
      val friendsName = readLine()
      friendsName
    }

    def makeFriends(): Unit = {
      greet()
      val friendsName = askName()
      println(s"Nice to meet you $friendsName")
    }
  }

  Friend("David").makeFriends()
}
