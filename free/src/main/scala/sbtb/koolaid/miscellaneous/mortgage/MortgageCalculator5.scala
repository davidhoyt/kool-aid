package sbtb.koolaid.miscellaneous.mortgage

object MortgageCalculator5 extends App {
  sealed trait IO[A] { self =>
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

//  case class Return[A](value: A) extends IO[A] {
//    override def run: A = value
//  }
//
//  case class Suspend[A](execute: => A) extends IO[A] {
//    override def run: A = execute
//  }

  //We'll modify PrintLine to capture the idea that it's side effecting.
  def PrintLine(line: String): IO[Unit] = new IO[Unit] {
    override def run(): Unit = println(line)
  }

  //Let's add ReadLine
  def ReadLine(): IO[String] = new IO[String] {
    override def run(): String = readLine()
  }

  //And then use a for comprehension to put it all together.
  val calculateMonthly =
    for {
      _ <- PrintLine("Mortgage amount (principal):")
      principal <- ReadLine()
      _ <- PrintLine("Annual interest rate (%):")
      interestRate <- ReadLine()
      _ <- PrintLine("Mortgage period (in years):")
      period <- ReadLine()
      monthlyPayment = Formulas.monthlyPayment(BigDecimal(principal), BigDecimal(interestRate), period.toInt)
      _ <- PrintLine(s"Your monthly payment will be $$${monthlyPayment.doubleValue()}")
    } yield ()

  //And then run it.
  calculateMonthly.run
}

// All these flat maps could blow the stack, so what can be done to fix it?
// Trampolining is a means whereby we can save off some work to do and lazily evaluate it later,
// similar to how a Scala stream can defer evaluation and thus enables tail recursion. In so doing,
// we can effectively freeze or suspend execution in bite-sized chunks and then continue when
// we please. Let's see how we'd set things up for trampolining.

//// Let's see if there's a general pattern that we can extract from what we just did.
//// 

// QA
//
// Why couldn't you put the last PrintLine() in the yield?
//
// Simply because of the way we've defined map. For comprehensions are desugared into calls to map, flatmap, foreach
// and others, and our current definition of map does not and cannot call the run method on a function because
// it's not an instance of IO. This implies that to actually run it, it must be called explicitly or be placed in
// the for comprehension that will be desugared into a call to flat map.