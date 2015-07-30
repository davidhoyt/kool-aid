package sbtb.koolaid.sjs

import org.scalajs.dom
import org.scalajs.dom.html

object Utils {
  def toBigDecimal(given: Option[String]): BigDecimal =
    BigDecimal(given.getOrElse("0"))

  def toInt(given: Option[String]): Int =
    given.getOrElse("0").toInt

  def inputValueFor(id: String, default: String): String = {
    inputValueFor(id).getOrElse(default)
  }

  def inputValueFor(id: String): Option[String] = {
    val maybeElement = Option(dom.document.getElementById(id))
    val maybeValue = maybeElement.map(_.asInstanceOf[html.Input].value).filter(_.nonEmpty)
    maybeValue
  }

  def swapBuffers(target: html.Div, buffer: html.Div): Unit = {
    target.innerHTML = ""
    target.appendChild(buffer)
  }
}
