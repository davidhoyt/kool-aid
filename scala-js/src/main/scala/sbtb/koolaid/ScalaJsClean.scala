package sbtb.koolaid

import org.scalajs.dom.html

import scala.scalajs.js.annotation.JSExport
import scalatags.JsDom.all._

@JSExport
object ScalaJsClean {
  @JSExport
  def main(target: html.Div): Unit = {

    target.appendChild(div(
      h1("Scala By the Bay"),
      input(
        `type`:="text",
        placeholder:="Type here!"
      )
    ).render)
  }
}
