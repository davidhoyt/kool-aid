package sbtb.koolaid

import sbt._
import sbt.Keys._

object publish {
  def ignoreSettings() = Seq[Setting[_]](
    Keys.publish := (),
    publishLocal := (),
    publishArtifact := false
  )

  def settings() = Seq[Setting[_]](
    autoAPIMappings := true,
    publishMavenStyle := true,
    publishArtifact in packageDoc := false,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false }
  )
}