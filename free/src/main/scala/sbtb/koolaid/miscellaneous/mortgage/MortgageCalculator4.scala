package sbtb.koolaid.miscellaneous.mortgage

//We'll start off by generalizing IO through the addition of a type parameter.
object MortgageCalculator4 extends App {
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

  //We'll modify PrintLine to capture the idea that it's side effecting.
  def PrintLine(line: String): IO[Unit] = new IO[Unit] {
    override def run(): Unit = println(line)
  }

  //Let's add ReadLine
  def ReadLine(): IO[String] = new IO[String] {
    override def run(): String = readLine()
  }

  def ReadLineEx[A]()(implicit convert: String => A): IO[A] =
    ReadLine() map convert

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

  // Consider what we've accomplished here.
  // We have effectively encoded an imperative program into our IO monad in a purely functional way. Although it's not
  // something we'll be covering today, it's possible to express other imperative constructs such as a loop.

  // Let's consider what would happen if we had many instances of IO that we had composed together.
  // Given a large enough application, we could overflow the runtime stack and get a stack overflow error.
  //
  // We'll start by defining a method to lift a value into the IO monad.
  def Unit[A](value: A) = new IO[A] {
    override def run: A = value
  }

  //
//  def repeat[A](n: Int, work: IO[A]): IO[A] = {
//    def step(index: Int, last: A): IO[A] = new IO[A] {
//      override def run: A = if (index == 0) last else step(index - 1, work.run)
//    }
//
//  }

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