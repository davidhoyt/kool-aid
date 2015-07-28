package sbtb.koolaid

import cats._, free._, Free._

import scala.io.StdIn

object CatsMortgageCalculator extends App {
  sealed trait Console[A]
  object Console {
    type Expression[A] = FreeC[Console, A]
    case class ReadLine[A](default: A, convert: String => Option[A]) extends Console[Option[A]]
    case class PrintLine(line: String) extends Console[Unit]

    def readLine[A](default: A)(implicit convert: String => Option[A]): Expression[Option[A]] =
      liftFC[Console, Option[A]](ReadLine(default, convert))

    def printLine(line: String): Expression[Unit] =
      liftFC[Console, Unit](PrintLine(line))

    def interpret[A, G[_] : Monad](prog: Expression[A])(t: Console ~> G) =
      runFC(prog)(t)
  }

  trait Implementation[F[_]] { self =>
    def extract[A](given: F[A]): A

    def ~>[G[_] : Monad]: (F ~> G) =
      Implementation.nat(self, implicitly[Monad[G]])
  }

  object Implementation {
    implicit def nat[F[_], G[_], I <: Implementation[F]](implicit I: I, G: Monad[G]) = new (F ~> G) {
      override def apply[A](given: F[A]): G[A] = G.pure(I.extract(given))
    }
  }

  object StdIOConsole extends Implementation[Console] {
    import Console._

    def extract[A](given: Console[A]): A =
      given match {
        case ReadLine(_, conv) => conv(StdIn.readLine())
        case PrintLine(line) => println(line)
      }
  }

  import Console._
  val nameProg: Console.Expression[String] = for {
       _ <- printLine("this is a test")
    name <- readLine("")
       _ <- printLine("hello " + name)
  } yield name + "???"

  val result: String = interpret(nameProg)(StdIOConsole ~> Id)
  println("GOT: " + result)
  //run(nameProg)

  //Use signals for composition???
}
