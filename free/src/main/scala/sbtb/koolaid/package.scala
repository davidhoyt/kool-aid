package sbtb

package object koolaid {
  implicit def convertString(given: String): Option[String] = Option(given)
}
