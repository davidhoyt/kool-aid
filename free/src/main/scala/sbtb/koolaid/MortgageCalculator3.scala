package sbtb.koolaid

//We'll create a trait named "IO" and add a map and flatMap method so we can compose IO
//instances with for comprehensions. We'll then add a new method called "run" which returns Unit
//to indicate that it's side effecting. Run will actually evaluate the side-effecting logic.
object MortgageCalculator3 extends App {
  trait IO { self =>
    def map(fn: Unit => Unit): IO = new IO {
      override def run: Unit = {
        fn(self.run())
      }
    }

    def flatMap(fn: Unit => IO): IO = new IO {
      override def run: Unit = {
        fn(self.run()).run()
      }
    }
    def run(): Unit
  }

  //Let's then create a method called PrintLine that will create a new IO instance
  //that represents printing a line of text to standard out.
  def PrintLine(line: String): IO = new IO {
    override def run(): Unit = println(line)
  }

  //We'll isolate output statements to their own IO instance and then compose them together to calculate the monthly payment.
  val calculateMonthly =
    PrintLine("Mortgage amount (principal):") flatMap { _ =>
      val principal = BigDecimal(readLine())
      PrintLine("Annual interest rate (%):") flatMap { _ =>
        val interestRate = BigDecimal(readLine())
        PrintLine("Mortgage period (in years):") flatMap { _ =>
          val period = readLine().toInt
          val monthlyPayment = Formulas.monthlyPayment(principal, interestRate, period)
          PrintLine(s"Your monthly payment will be $$${monthlyPayment.doubleValue()}")
        }
      }
    }

  //At this point, we can define how a series of side-effects should be evaluated without actually executing it.
  //In order to actually find the monthly payment, we'll need to execute the IO we've created.
  calculateMonthly.run
}

//You'll notice that we have only made a print line. Let's add read line and then do some refactoring.
