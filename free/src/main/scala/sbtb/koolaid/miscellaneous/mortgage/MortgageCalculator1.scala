package sbtb.koolaid.miscellaneous.mortgage

import scala.io.StdIn

//Mortgage amount
//Annual interest rate (%)
//Mortgage period (in years)
object MortgageCalculator1 extends App {
  println("Mortgage amount (principal):")
  val principal = BigDecimal(StdIn.readLine())

  println("Annual interest rate (%):")
  val interestRate = BigDecimal(StdIn.readLine())

  println("Mortgage period (in years):")
  val period = StdIn.readLine().toInt

  val monthlyPayment = Formulas.monthlyPayment(principal, interestRate, period)
  println(s"Your monthly payment will be $$${monthlyPayment.doubleValue()}")
}

//We can do a simple refactoring that will isolate the request and response cycle for each part of the calculation.
//This will give us:
