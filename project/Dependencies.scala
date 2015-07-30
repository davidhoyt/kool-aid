package sbtb.koolaid

import sbt._
import org.scalajs.sbtplugin.ScalaJSPlugin
import ScalaJSPlugin.autoImport._

object dependencies {
  val catsVersion = "0.1.3-SNAPSHOT"
  val shapelessVersion = "2.2.4"

  val akkaVersion = "2.3.12"
  val akkaStreamVersion = "1.0"

  val scalaJsLibraryVersion = "0.6.4"
  val scalaJsDomVersion = "0.8.0"
  val scalaTagsVersion = "0.4.6"
  val monifuVersion = "1.0-M1"

  val scalaTestVersion = "2.2.4"
  val scalaCheckVersion = "1.12.2"

  lazy val overrides = Set(joda ++ akka :_*)

  val joda = Seq(
    "joda-time" % "joda-time" % "2.7" exclude("org.joda", "joda-convert"),
    "org.joda" % "joda-convert" % "1.7"
  )

  val hocon = Seq(
    "org.apache.commons" % "commons-io" % "1.3.2",
    "net.ceedubs" %% "ficus" % "1.1.2" exclude("com.typesafe", "config"),
    "com.typesafe" % "config" % "1.3.0"
  )

  val rxScala = Seq(
    "io.reactivex" %% "rxscala" % "0.24.1"
  )

  val akka = Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
    "com.typesafe.akka" %% "akka-agent" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream-experimental" % akkaStreamVersion,
    "com.typesafe.akka" %% "akka-http-core-experimental" % akkaStreamVersion,
    "com.typesafe.akka" %% "akka-http-experimental" % akkaStreamVersion,
    //
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test
  )

  val unitTesting = Seq(
    "org.scalatest" %% "scalatest" % scalaTestVersion % Test,
    "org.scalacheck" %% "scalacheck" % scalaCheckVersion % Test
  )
}
