package sbtb.koolaid

import sbtb.koolaid.twitter.client._

object HelloStep2 extends JvmApp {
  trait IO[A] { self =>
    def run: A

    def map[B](fn: A => B): IO[B] = new IO[B] {
      def run = fn(self.run)
    }

    def flatMap[B](fn: A => IO[B]) = new IO[B] {
      override def run: B = fn(self.run).run
    }
  }

  object ConsoleIO {
    sealed trait ConsoleIO[A] extends IO[A]
    case class Tell(text: String, default: String) extends ConsoleIO[Unit] { def run = prompt(text, default) }
    case class Ask(default: String) extends ConsoleIO[String] { def run = readPrompt(default)}

    def tell(text: String, default: String = ""): ConsoleIO[Unit] = Tell(text, default)
    def ask(default: String): ConsoleIO[String] = Ask(default)
  }

  import ConsoleIO._

  val program =
    for {
         _ <- tell("Your name please:", "Conan")
      name <- ask("Conan")
         _ <- tell(s"Hello $name")
    } yield ()

  //program.run
  program.run
}
