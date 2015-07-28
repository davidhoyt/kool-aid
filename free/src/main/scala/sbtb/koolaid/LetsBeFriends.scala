package sbtb.koolaid

object LetsBeFriends extends App {
  case class Friend(firstName: String) {
    def makeFriends(): Unit = {
      println(s"Hi, I'm $firstName.")
      println("What's your name?")
      val friendsName = readLine()
      println(s"Nice to meet you $friendsName")
    }
  }
  
  Friend("David").makeFriends()
}
