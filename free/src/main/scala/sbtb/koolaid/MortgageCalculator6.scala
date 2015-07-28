package sbtb.koolaid

import cats.{Applicative, Comonad}

import scala.io.StdIn
import scala.util.Try

object MortgageCalculator6 extends App {

  //Monad as a type class
  trait Monad[M[_]] {
    def unit[A](given: A): M[A]
    def map[A, B](given: M[A])(fn: A => B): M[B]
    def flatMap[A, B](given: M[A])(fn: A => M[B]): M[B]
  }

  trait Natural[F[_], G[_]] {
    def apply[A](given: F[A]): G[A]
  }

  object Natural {
    implicit def identity[F[_]] = new (F ~> F) {
      override def apply[A](given: F[A]): F[A] = given
    }
  }

  type ~>[F[_], G[_]] = Natural[F, G]

  sealed trait Free[F[_], A] { self =>
    def map[B](fn: A => B): Free[F, B] =
      flatMap((a: A) => Return(fn(a)))

    def flatMap[B](fn: A => Free[F, B]): Free[F, B] =
      FlatMap[F, A, B](self, fn)
  }

  case class Return[F[_], A](value: A) extends Free[F, A]
  case class Suspend[F[_], A](fa: F[A]) extends Free[F, A]
  case class FlatMap[F[_], A, B](s: Free[F, A], f: A => Free[F, B]) extends Free[F, B]

  //Comonad-ic
  trait Implementation[F[_]] { self =>
    def run[A](given: F[A]): A

    def ~>[G[_] : Monad]: (F ~> G) =
      Implementation.nat(self, implicitly[Monad[G]])
  }

  object Implementation {
    implicit def nat[F[_], G[_], I <: Implementation[F]](implicit I: I, G: Monad[G]) = new (F ~> G) {
      override def apply[A](given: F[A]): G[A] = G.unit(I.run(given))
    }
  }

  sealed trait Console[A]
  object Console {
    case class ReadLine[A](default: A, convert: String => Option[A]) extends Console[Option[A]]
    case class PrintLine(line: String) extends Console[Unit]

    def readLine[A](default: A = {})(implicit convert: String => Option[A]): Free[Console, Option[A]] =
      Suspend[Console, Option[A]](ReadLine[A](default, convert))

    def printLine(line: String): Free[Console, Unit] =
      Suspend[Console, Unit](PrintLine(line))
  }

  implicit object Function0Monad extends Monad[Function0] {
    override def unit[A](given: A): () => A = () => given

    override def flatMap[A, B](given: () => A)(fn: A => () => B): () => B = fn(given())

    override def map[A, B](given: () => A)(fn: A => B): () => B = () => fn(given())
  }

  object StdIOConsole extends Implementation[Console] {
    import Console._

    def run[A](given: Console[A]): A =
      given match {
        case ReadLine(_, conv) => conv(StdIn.readLine())
        case PrintLine(line) => println(line)
      }
  }

  import scala.concurrent._
  import scala.concurrent.duration.DurationInt
  import ExecutionContext.Implicits.global

  implicit def Concurrently(implicit ec: ExecutionContext): Monad[Future] = new Monad[Future] {
    override def unit[A](given: A): Future[A] =
      Future.successful(given)

    override def map[A, B](given: Future[A])(fn: A => B): Future[B] =
      given map fn

    override def flatMap[A, B](given: Future[A])(fn: A => Future[B]): Future[B] =
      given flatMap fn
  }


  // The Return and FlatMap constructors witness that this data type is a monad for any choice of F,
  // and since they’re exactly the operations required to generate a monad, we say that it’s a free monad.[9]

  // “Free” in this context means generated freely in the sense that F itself doesn’t need to have any
  // monadic structure of its own.

  // What is the meaning of Free[F,A]? Essentially, it’s a recursive structure that contains a value of type A
  // wrapped in zero or more layers of F.[11] It’s a monad because flatMap lets us take the A and from it
  // generate more layers of F. Before getting at the result, an interpreter of the structure must be able to
  // process all of those F layers. We can view the structure and its interpreter as coroutines that are
  // interacting, and the type F defines the protocol of this interaction. By choosing our F carefully, we can
  // precisely control what kinds of interactions are allowed.

  // Let's show that we can create a monad for any type constructor F
  def freeMonad[F[_]]: Monad[({type f[a] = Free[F,a]})#f] = new Monad[({type f[a] = Free[F,a]})#f] {
    override def unit[A](given: A): Free[F, A] = Return[F, A](given)

    override def flatMap[A, B](given: Free[F, A])(fn: (A) => Free[F, B]): Free[F, B] =
      FlatMap(given, fn)

    override def map[A, B](given: Free[F, A])(fn: (A) => B): Free[F, B] =
      flatMap(given)(a => Return(fn(a)))
  }

  @annotation.tailrec
  def step[F[_], A](free: Free[F, A]): Free[F, A] = free match {
    case FlatMap(FlatMap(x, f), g) => step(x flatMap (a => f(a) flatMap g))
    case FlatMap(Return(x), f) => step(f(x))
    case _ => free
  }

  def runFree[F[_], G[_], A](free: Free[F, A])(t: F ~> G)(implicit G: Monad[G]): G[A] = {
    step(free) match {
      case Return(a) => G.unit(a)
      case Suspend(r) => t(r)
      case FlatMap(Suspend(r), f) => G.flatMap(t(r))(a => runFree(f(a))(t))
      case error => sys.error(s"Impossible; `step` eliminates these cases: $error")
    }
  }

  implicit class SafeNumberParse(val s: String) extends AnyVal {
    def toDoubleSafely: Option[Double] = Try(s.toDouble).toOption
    def toIntSafely: Option[Int] = Try(s.toInt).toOption
  }

  import Console._

  def mortgageAmount() = for {
    _ <- printLine("Mortgage amount (principal):")
    principal <- readLine(1000000.0)(_.toDoubleSafely)
  } yield principal

  def interestRate() = for {
    _ <- printLine("Annual interest rate (%):")
    interestRate <- readLine(2.99)(_.toDoubleSafely)
  } yield interestRate

  def mortgagePeriod() = for {
    _ <- printLine("Mortgage period (in years):")
    period <- readLine(15)(_.toIntSafely)
  } yield period

  def monthlyPayment() =
    for {
      prin <- mortgageAmount()
      interest <- interestRate()
      per <- mortgagePeriod()
      monthly = Formulas.monthlyPayment(prin.getOrElse(0.0), interest.getOrElse(0.0), per.getOrElse(1))
    } yield monthly

//  val got: Future[String] = runFree(myProg)(StdIOConsole ~> Concurrently)
//  println("I GOT: " + Await.result(got, 10.seconds))

  val monthly = runFree(monthlyPayment())(StdIOConsole ~> Function0Monad) //(Implementation.nat(Function0Monad, StdIOConsole)) //(ConsoleImplementation.fromConsole(StdIOConsole, Function0Monad))
  println(f"Your monthly payment will be $$${monthly()}%.2f")
}
