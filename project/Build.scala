package sbtb.koolaid

import sbt._
import sbt.Keys._

object build {
  val defaultScalaVersion = "2.11.7"
  val defaultCrossScalaVersions = Seq(defaultScalaVersion)

  val resolvers = Seq[Resolver](
    "Typesafe" at "https://repo.typesafe.com/typesafe/releases/",
    "non-bintray" at "http://dl.bintray.com/non/maven",
    "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
    "Atlassian" at "https://maven.atlassian.com/public/",
    Resolver.sonatypeRepo("releases"),
    Resolver.url("Typesafe repository, Ivy", url("https://dl.bintray.com/typesafe/ivy-releases/"))(Resolver.ivyStylePatterns)
  ) ++ DefaultOptions.resolvers(snapshot = true)

  val scalacOptions = Seq[String](
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-language:experimental.macros",
    "-unchecked"
  )

  val javacOptions = Seq[String](
    "-source", "1.8",
    "-target", "1.8"
  )

  def settings(scalaVersion: String = defaultScalaVersion, crossScalaVersions: Seq[String] = defaultCrossScalaVersions) =
    evictions.settings ++ Seq[Setting[_]](
      organization := "sbtb",
      Keys.scalaVersion := scalaVersion,
      Keys.crossScalaVersions := crossScalaVersions,
      Keys.resolvers ++= resolvers,
      Keys.scalacOptions ++= scalacOptions,
      Keys.javacOptions ++= javacOptions,
      fork in run := false //org.apache.commons.lang3.SystemUtils.IS_OS_UNIX
    )
}
