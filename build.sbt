import sbtb.koolaid.dependencies._
import com.lihaoyi.workbench.Plugin._

lazy val `sbtb-koolaid-root` = (project in file("."))
  .aggregate(free, `scala-js`, twitter)
  .dependsOn(free, `scala-js`, twitter)
  .settings(sbtb.koolaid.publish.ignoreSettings())
  .settings(sbtb.koolaid.build.settings())
  .settings(Seq(
    name := "sbtb-koolaid-root"
  ))

lazy val free = (project in file("free"))
  .settings(sbtb.koolaid.publish.settings())
  .settings(sbtb.koolaid.build.settings())
  .settings(Seq(
    name := "sbtb-koolaid-free",
    moduleName := "free",
    libraryDependencies ++= { Seq(
        "com.chuusai" %%% "shapeless" % shapelessVersion,
        "org.spire-math" %% "cats" % catsVersion
      )++
      unitTesting
    }
  ))

lazy val `scala-js` = (project in file("scala-js"))
  .dependsOn(free)
  .enablePlugins(ScalaJSPlugin)
  .settings(sbtb.koolaid.publish.settings())
  .settings(sbtb.koolaid.build.settings())
  .settings(workbenchSettings)
  .settings(Seq(
    name := "sbtb-koolaid-scala-js",
    moduleName := "scala-js",
    bootSnippet := "sbtb.koolaid.sjs.MortgageCalculator().main(document.getElementById('content'), document.getElementById('result'));",
    updateBrowsers <<= updateBrowsers.triggeredBy(fastOptJS in Compile),
    libraryDependencies ++= { Seq(
      "com.chuusai" %%% "shapeless" % shapelessVersion,
      "org.spire-math" %% "cats" % catsVersion,
      "org.scala-js" %%% "scalajs-dom" % scalaJsDomVersion,
      "com.lihaoyi" %%% "scalatags" % scalaTagsVersion,
      "org.monifu" %%% "monifu" % monifuVersion
      ) ++
      unitTesting
    }
  ))

lazy val twitter = (project in file("twitter"))
  .settings(sbtb.koolaid.publish.settings())
  .settings(sbtb.koolaid.build.settings())
  .settings(Seq(
    name := "sbtb-koolaid-twitter",
    moduleName := "twitter",
    libraryDependencies ++= { Seq(
        "com.chuusai" %% "shapeless" % shapelessVersion,
        "org.spire-math" %% "cats" % catsVersion
      ) ++
      joda ++
      hocon ++
      akka ++
      rxScala ++
      unitTesting
    }
  ))

//enablePlugins(ScalaJSPlugin)
//
//workbenchSettings
//
//name := "Drinking the free kool-aid"
//
//version := "1.0.0"
//
//scalaVersion := "2.11.7"

//libraryDependencies ++= Seq(
//  "com.chuusai" %%% "shapeless" % "2.2.4",
//  "org.spire-math" %% "cats" % "0.1.3-SNAPSHOT",
//  "org.scala-js" %%% "scalajs-dom" % "0.8.0",
//  "com.lihaoyi" %%% "scalatags" % "0.4.6",
//  "org.monifu" %%% "monifu" % "1.0-M1"
//)
//
//bootSnippet := "sbtb.koolaid.ScalaJs().main(document.getElementById('content'));"
//
//updateBrowsers <<= updateBrowsers.triggeredBy(fastOptJS in Compile)
