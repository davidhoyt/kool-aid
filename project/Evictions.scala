package sbtb.koolaid

import sbt._
import sbt.Keys._

object evictions {
  val warningOptions = EvictionWarningOptions.default.withWarnScalaVersionEviction(false)

  val settings = Seq[Setting[_]](
    dependencyOverrides := dependencies.overrides,
    evictionWarningOptions in update := warningOptions,
    evictionWarningOptions in evicted := warningOptions
  )
}
