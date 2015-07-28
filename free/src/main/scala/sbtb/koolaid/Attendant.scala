package sbtb.koolaid

sealed abstract class Drink(val cost: Double = 0.0D)
case object Coffee extends Drink()
case object AppleJuice extends Drink()
case object DewarsScotch extends Drink(cost = 8.0D)

case class Airline(name: String)

object Drink {
  def apply(name: String): Option[Drink] = name.toLowerCase match {
    case "coffee" => Some(Coffee)
    case "apple juice" => Some(AppleJuice)
    case "scotch" | "dewars" => Some(DewarsScotch)
    case _ => None
  }
}

case class Attendant(firstName: String, airline: Airline) {
  def serveDrink(): Unit = {
    println(s"Welcome aboard ${airline.name}")
    println(s"What would you like to drink?")
    val drink = Drink(readLine())
    if (drink.isEmpty) {
      println("Sorry, that drink is not available")
    }
    if (drink.get.cost > 0) {
      println(s"That will be ${drink.get.cost} please")
    }
  }
}

object Attendant extends App {
  //println(s"Welcome to $")
}
