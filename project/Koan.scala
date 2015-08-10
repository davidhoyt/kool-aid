package sbtb.koolaid

import com.typesafe.training.sbtkoan.SbtKoan.autoImport._
import sbt._

object koan {
  def settings(): Seq[Setting[_]] = Seq(
    configurations := Set(Configurations.Sources, Configurations.Test),
    historyRef     := "master",
    initial        := "step:initial",
    ignoreCommit   := "ignore"
  )
}
