import sbtb.koolaid.dependencies._
import com.lihaoyi.workbench.Plugin._

addCommandAlias("run-twitter", ";project twitter;run")
addCommandAlias("run-scala-js", ";project scala-js;~fastOptJS")

lazy val `sbtb-koolaid-root` = (project in file("."))
  .aggregate(free, `scala-js`, twitter)
  .dependsOn(free, `scala-js`, twitter)
  .settings(sbtb.koolaid.publish.ignoreSettings())
  .settings(sbtb.koolaid.build.settings())
  .settings(Seq(
    name := "drinking the free kool aid",
    moduleName := "sbtb-koolaid"
  ))

lazy val free = (project in file("free"))
  .enablePlugins(ScalaJSPlugin)
  .settings(sbtb.koolaid.publish.settings())
  .settings(sbtb.koolaid.build.settings())
  .settings(Seq(
    name := "sbtb-koolaid-free",
    moduleName := "free",
    libraryDependencies ++= { Seq(
        "com.chuusai" %%% "shapeless" % shapelessVersion,
        "org.spire-math" %% "cats" % catsVersion//,
        //"org.scala-js" %%% "scalajs-library" % scalaJsLibraryVersion
      ) ++
      unitTesting
    }
  ))

lazy val `scala-js` = (project in file("scala-js"))
  .aggregate(free)
  .dependsOn(free)
  .enablePlugins(ScalaJSPlugin)
  .settings(sbtb.koolaid.publish.settings())
  .settings(sbtb.koolaid.build.settings())
  .settings(workbenchSettings)
  .settings(Seq(
    name := "sbtb-koolaid-scala-js",
    moduleName := "scala-js",
    bootSnippet := "sbtb.koolaid.sjs.MortgageCalculator().main(document.getElementById('content'), document.getElementById('result'));",
    refreshBrowsers <<= refreshBrowsers.triggeredBy(fastOptJS in Compile),
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
