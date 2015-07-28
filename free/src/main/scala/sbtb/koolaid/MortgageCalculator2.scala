package sbtb.koolaid

//Mortgage amount
//Annual interest rate (%)
//Mortgage period (in years)
object MortgageCalculator2 extends App {
  def retrievePrincipal(): BigDecimal = {
    println("Mortgage amount (principal):")
    BigDecimal(readLine())
  }

  def retrieveAnnualInterestRate(): BigDecimal = {
    println("Annual interest rate (%):")
    BigDecimal(readLine())
  }

  def retrievePeriod(): Int = {
    println("Mortgage period (in years):")
    readLine().toInt
  }

  val monthlyPayment = Formulas.monthlyPayment(retrievePrincipal(), retrieveAnnualInterestRate(), retrievePeriod())
  println(s"Your monthly payment will be $$${monthlyPayment.doubleValue()}")
}

//What have we gained by moving the work into individual methods? For such a simple calculation, not seemingly a
//lot. It does provide for the option to loop upon receipt of invalid data had we chosen to do so.
//It has not given a significant advantage for readability.
//
//Suppose I were to ask you to derive a way to reason about IO in a pure and referentially transparent way.
//It would be difficult to come to a solution since IO is fundamentally about side effects. In order for us
//to reason about IO, perhaps we could stage effects and defer evaluation until later.
//
//How can we achieve that? Let's begin by introducing a new trait that can be used to describe input and output.
//I'll issue a warning that with regard to these practices and their application, one should carefully weigh
//their utility for your situation. It may be too much effort for a typical application without enough gain. Consider,
//though, the ability to reason about, isolate, and push effects to the boundaries which enables us to combine and
//compose effects just like functions. In other words, your mileage may vary (YMMV).
