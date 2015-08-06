import sbtb.koolaid.dependencies._

addCommandAlias("run-twitter", "; project twitter-server ; run")
addCommandAlias("run-scala-js", "; project scala-js ; ~fastOptJS")

lazy val `sbtb-koolaid-root` = (project in file("."))
  .aggregate(`twitter-client`, free, `scala-js`, `twitter-server`)
  .dependsOn(`twitter-client`, free, `scala-js`, `twitter-server`)
  .settings(sbtb.koolaid.publish.ignoreSettings())
  .settings(sbtb.koolaid.build.settings())
  .settings(Seq(
    name := "drinking the free kool aid",
    moduleName := "sbtb-koolaid"
  ))

lazy val free = (project in file("free"))
  .aggregate(`twitter-client`)
  .dependsOn(`twitter-client`)
  .enablePlugins(ScalaJSPlugin)
  .settings(sbtb.koolaid.publish.settings())
  .settings(sbtb.koolaid.build.settings())
  .settings(Seq(
    name := "sbtb-koolaid-free",
    moduleName := "free",
    libraryDependencies ++= { Seq(
        "com.chuusai" %%% "shapeless" % shapelessVersion,
        "org.spire-math" %% "cats" % catsVersion
      ) ++
      unitTesting
    }
  ))

lazy val `scala-js` = (project in file("scala-js"))
  .aggregate(`twitter-client`, free)
  .dependsOn(`twitter-client`, free)
  .enablePlugins(ScalaJSPlugin)
  .settings(sbtb.koolaid.publish.settings())
  .settings(sbtb.koolaid.build.settings())
  //.settings(workbenchSettings)
  .settings(Seq(
    name := "sbtb-koolaid-scala-js",
    moduleName := "scala-js",
    libraryDependencies ++= { Seq(
      "com.chuusai" %%% "shapeless" % shapelessVersion,
      "org.spire-math" %% "cats" % catsVersion,
      "org.scala-js" %%% "scalajs-dom" % scalaJsDomVersion,
      "com.lihaoyi" %%% "scalatags" % scalaTagsVersion,
      "com.github.benhutchison" %%% "prickle" % prickleVersion,
      "org.monifu" %%% "monifu" % monifuVersion
      ) ++
      unitTesting
    }
  ))

lazy val `twitter-client` = (project in file("twitter-client"))
  .enablePlugins(ScalaJSPlugin)
  .settings(sbtb.koolaid.publish.settings())
  .settings(sbtb.koolaid.build.settings())
  .settings(Seq(
    name := "sbtb-koolaid-twitter-client",
    moduleName := "twitter-client",
    libraryDependencies ++= { Seq(
        "com.chuusai" %% "shapeless" % shapelessVersion,
        "org.spire-math" %% "cats" % catsVersion,
        "org.scala-js" %%% "scalajs-dom" % scalaJsDomVersion,
        "com.lihaoyi" %%% "autowire" % autowireVersion,
        "com.lihaoyi" %%% "upickle" % uPickleVersion,
        "com.github.benhutchison" %%% "prickle" % prickleVersion
      ) ++
      joda ++
      hocon ++
      akka ++
      rxScala ++
      unitTesting
    }
  ))

lazy val `twitter-server` = (project in file("twitter-server"))
  .aggregate(`twitter-client`)
  .dependsOn(`twitter-client`)
  .disablePlugins(ScalaJSPlugin)
  .settings(sbtb.koolaid.publish.settings())
  .settings(sbtb.koolaid.build.settings())
  .settings(Seq(
    name := "sbtb-koolaid-twitter-server",
    moduleName := "twitter-server",
    libraryDependencies ++= { Seq(
        "com.chuusai" %% "shapeless" % shapelessVersion,
        "org.spire-math" %% "cats" % catsVersion,
        "com.lihaoyi" %% "autowire" % autowireVersion,
        "com.lihaoyi" %% "upickle" % uPickleVersion,
        "com.github.benhutchison" %% "prickle" % prickleVersion,
        "org.twitter4j" % "twitter4j-core" % twitter4jVersion
      ) ++
      joda ++
      hocon ++
      akka ++
      rxScala ++
      unitTesting
    }
  ))
