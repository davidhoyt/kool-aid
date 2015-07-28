package sbtb.koolaid

import scala.io.StdIn

//Mortgage amount
//Annual interest rate (%)
//Mortgage period (in years)
object MortgageCalculator extends App {
  trait TailRec[A] { self =>
    def run: A

    def map[B](fn: A => B): TailRec[B] = new TailRec[B] {
      override def run: B =
        fn(self.run)
    }

    def flatMap[B](fn: A => TailRec[B]): TailRec[B] = new TailRec[B] {
      override def run: B =
        fn(self.run).run
    }
  }

//  sealed trait TailRec[A] { self =>
//    def flatMap[B](fn: A => TailRec[B]): TailRec[B] =
//      FlatMap(self, fn)
//
//    def map[B](fn: A => B): TailRec[B] =
//      flatMap((a: A) => Return(fn(a)))
//  }
//
//  case class Return[A](value: A) extends TailRec[A]
//  case class Suspend[A](work: Function0[A]) extends TailRec[A]
//  case class FlatMap[A, B](sub: TailRec[A], k: A => TailRec[B]) extends TailRec[B]

//  sealed trait Free[F[_], A] { self =>
//    def flatMap[B](fn: A => Free[F, B]): Free[F, B] =
//      FlatMap(self, fn)
//
//    def map[B](fn: A => B): Free[F, B] =
//      flatMap((a: A) => Return(fn(a)))
//  }
//
////  case class Return[F[_], A](value: A) extends Free[F, A]
////  case class Suspend[F[_], A](work: F[A]) extends Free[F, A]
////  case class FlatMap[F[_], A, B](sub: Free[F, A], k: A => Free[F, B]) extends Free[F, B]
////
////  def run[F[_], A](given: Free[F, A]): A = given match {
////    case Return(a) => a
////    case Suspend(f) => f()
////    case FlatMap(x, f) => x match {
////      case Return(a) => f(a)
////      case Suspend(g) =>
////    }
////  }

//  def forever[A](io: TailRec[A]): TailRec[A] = {
//    lazy val fm: TailRec[A] = FlatMap(io, (a: A) => fm)
//    fm
//  }

//  def Println(output: String): TailRec[Unit] =
//    Suspend(() => Return(println(output)))
//
//  def Readline(): TailRec[String] =
//    Suspend(() => StdIn.readLine())
//
//  def mortgageAmount(): TailRec[BigDecimal] =
//    for {
//      _ <- Println("Mortgage amount (principal):")
//      sPrincipal <- Readline()
//    } yield BigDecimal(sPrincipal)
//
//  def interestRate(): TailRec[BigDecimal] =
//    for {
//      _ <- Println("Annual interest rate (%):")
//      sInterestRate <- Readline()
//    } yield BigDecimal(sInterestRate)
//
//  def period(): TailRec[Int] =
//    for {
//      _ <- Println("Mortgage period (in years):")
//      sPeriod <- Readline()
//    } yield sPeriod.toInt
//
//  def monthlyPayment(principal: BigDecimal, interestRate: BigDecimal, period: Int): TailRec[BigDecimal] = {
//    val monthlyPayment = Formulas.monthlyPayment(principal, interestRate, period)
//    for {
//      _ <- Println(s"Your monthly payment will be $$${monthlyPayment.doubleValue()}")
//    } yield monthlyPayment
//  }
//
//  val calculateMortgagePayments: TailRec[Unit] =
//    for {
//      principal <- mortgageAmount()
//      interestRate <- interestRate()
//      period <- period()
//      _ <- monthlyPayment(principal, interestRate, period)
//    } yield ()
//
////  sealed trait Trampolining[A] {
////    def run: A
////  }
////  case class Return[A](value: => A) extends Trampolining[A] {
////    override def run: A = value
////  }
////  case class Suspend[A](work: => A)
//
////  def run[A](io: TailRec[A]): A = io match {
////    case Return(a) => a
////    case Suspend(fn) => fn()
////    case FlatMap(x, f) => x match {
////      case Return(a) => run(f(a))
////      case Suspend(fn) => run(f(fn()))
////      case FlatMap(y, g) => run(y flatMap (a => g(a) flatMap f))
////
////    }
////  }
//
////  def run[A](io: TailRec[A]): A = io match {
////    case Return(a) => a
////    case Suspend(f) => f()
////    case FlatMap(x, f) => x match {
////      case Return(a) => run(f(a))
////      case Suspend(g) => run(f(g()))
//////      case FlatMap(y, g) => run(y flatMap (a => g(a) flatMap f))
////      case FlatMap(y, g) => run(y flatMap (a => g(a) flatMap f)) //Reassociate to the right
////    }
////  }
//
//  def run[A](io: TailRec[A]): A = io match {
//    case Return(a) => a
//    case Suspend(f) => f()
//    case FlatMap(x, f) => x match {
//      case Return(a) => run(f(a))
//      case Suspend(g) => run(f(g()))
//      case FlatMap(y, g) => run(y flatMap (a => g(a) flatMap f))
//    }
//  }
//
//  //run(forever(Println("test this")))
//
//  run(calculateMortgagePayments)
////  calculateMortgagePayments.run
}

//We can do a simple refactoring that will isolate the request and response cycle for each part of the calculation.
//This will give us:
