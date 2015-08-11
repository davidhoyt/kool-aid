package sbtb.koolaid.miscellaneous

package object mortgage {
  implicit def convertString(given: String): Option[String] = Option(given)
}
