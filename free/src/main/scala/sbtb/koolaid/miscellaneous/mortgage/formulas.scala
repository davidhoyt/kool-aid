package sbtb.koolaid.miscellaneous.mortgage

object Formulas {
  import scala.math._

  def monthlyPayment(principal: BigDecimal, annualInterest: BigDecimal, yearsOfPayments: Int): BigDecimal = {
    val monthlyInterest: BigDecimal = annualInterest / 100 / 12
    val totalNumberOfPayments: Int = yearsOfPayments * 12
    val adjust: BigDecimal = (1 + monthlyInterest) pow totalNumberOfPayments

    if (adjust > 1)
      (principal * (monthlyInterest * adjust)) / (adjust - 1)
    else
      0
  }

  def monthlyPayment(principal: Double, annualInterest: Double, yearsOfPayments: Int): Double = {
    val monthlyInterest: Double = annualInterest / 100 / 12
    val totalNumberOfPayments: Int = yearsOfPayments * 12
    val adjust: Double = pow(1 + monthlyInterest, totalNumberOfPayments)

    if (adjust > 1)
      (principal * (monthlyInterest * adjust)) / (adjust - 1)
    else
      0
  }
  
  def main(args: Array[String]) = {
    println(monthlyPayment(400000, 3.5, 15)) //
  }
}
