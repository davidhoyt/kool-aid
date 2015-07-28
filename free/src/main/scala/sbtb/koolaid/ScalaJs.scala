package sbtb.koolaid

import org.scalajs.dom.raw.{MouseEvent, Node}

import scala.io.StdIn
import scalajs.js
import scalajs.js.annotation.JSExport
import scalatags.JsDom.all._
import org.scalajs.dom
import dom.html

@JSExport
object ScalaJs {
  import sjs.Utils._

  sealed trait TailRec[A] { self =>
    def flatMap[B](fn: A => TailRec[B]): TailRec[B] =
      FlatMap(self, fn)

    def map[B](fn: A => B): TailRec[B] =
      flatMap((a: A) => Return(fn(a)))
  }

  case class Return[A](value: A) extends TailRec[A]
  case class Suspend[A](work: () => A) extends TailRec[A]
  case class FlatMap[A, B](sub: TailRec[A], k: A => TailRec[B]) extends TailRec[B]

  case class JsItem[A](render: () => Node, fetch: () => A)
  object JsRender {
    def apply[A](render: => Node = span().render, fetch: => A): JsItem[A] =
      JsItem(() => render, () => fetch)
  }

  def Println(output: String): TailRec[JsItem[Unit]] =
    Return(JsRender(
      div(`class` := "label", output).render,
      ()
    ))

  def Readline(fieldId: String, default: String = ""): TailRec[JsItem[Option[String]]] =
    Return(JsRender(
        input(
          `type` := "text",
          id := fieldId,
          value := inputValueFor(fieldId, default)
        ).render,
      inputValueFor(fieldId)
    ))

  def mortgageAmount(): TailRec[JsItem[BigDecimal]] =
    for {
      label <- Println("Mortgage amount (principal):")
      principal <- Readline("amount", "1000000")
    } yield JsRender(div(label.render(), principal.render()).render, toBigDecimal(principal.fetch()))

  def interestRate(): TailRec[JsItem[BigDecimal]] =
    for {
      label <- Println("Annual interest rate (%):")
      interestRate <- Readline("interest", "2.99")
    } yield JsRender(div(label.render(), interestRate.render()).render, toBigDecimal(interestRate.fetch()))

  def period(): TailRec[JsItem[Int]] =
    for {
      label <- Println("Mortgage period (in years):")
      period <- Readline("period", "15")
    } yield JsRender(div(label.render(), period.render()).render, toInt(period.fetch()))

  def monthlyPayment(principal: BigDecimal, interestRate: BigDecimal, period: Int): TailRec[JsItem[BigDecimal]] = {
    val monthlyPayment = Formulas.monthlyPayment(principal, interestRate, period)
    Return(JsRender(fetch = monthlyPayment))
  }

  val calculateMortgagePayments: TailRec[JsItem[BigDecimal]] =
    for {
      principal <- mortgageAmount()
      interestRate <- interestRate()
      period <- period()
      result <- monthlyPayment(principal.fetch(), interestRate.fetch(), period.fetch())
    } yield JsRender(div(principal.render(), interestRate.render(), period.render(), result.render()).render, result.fetch())

  def run[A](io: TailRec[A]): A = io match {
    case Return(a) => a
    case Suspend(f) => f()
    case FlatMap(x, f) => x match {
      case Return(a) => run(f(a))
      case Suspend(g) => run(f(g()))
      case FlatMap(y, g) => run(y flatMap (a => g(a) flatMap f))
    }
  }

  @JSExport
  def main(content: html.Div, result: html.Div): Unit = {

    def render(): Unit = {
      val monthly: JsItem[BigDecimal] = run(calculateMortgagePayments)
      val ui = monthly.render()
      val buffer = div(
        ui,
        renderButton()
      ).render
      swapBuffers(content, buffer)
    }

    def calculateMonthly(): Unit = {
      val monthly: JsItem[BigDecimal] = run(calculateMortgagePayments)
      val payment = monthly.fetch()
      val buffer =
        div(
          span(s"Monthly payment: "),
          span(
            `class` := "monthly",
            f"$$$payment%.2f"
          )
        ).render
      swapBuffers(result, buffer)
    }

    def renderButton() = {
      val btn = button("Calculate").render
      btn.onclick = (_: MouseEvent) => calculateMonthly()
      btn
    }

    render()
    calculateMonthly()
  }
}
