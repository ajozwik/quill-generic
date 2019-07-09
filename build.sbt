import com.sksamuel.scapegoat.sbt.ScapegoatSbtPlugin.autoImport._
import scalariform.formatter.preferences._


val `scalaVersion_2.13` = "2.13.0"

val `scalaVersion_2.12` = "2.12.8"

ThisBuild / scapegoatVersion := "1.3.9"

val supportedScalaVersions = Seq(`scalaVersion_2.13`, `scalaVersion_2.12`)

ThisBuild / crossScalaVersions := supportedScalaVersions

ThisBuild / scalaVersion := `scalaVersion_2.12`

ThisBuild / organization := "com.github.ajozwik"

ThisBuild / scalacOptions ++= Seq(
  "-target:jvm-1.8",
  "-encoding", "UTF-8",
  "-deprecation", // warning and location for usages of deprecated APIs
  "-feature", // warning and location for usages of features that should be imported explicitly
  "-unchecked", // additional warnings where generated code depends on assumptions
  "-Xlint", // recommended additional warnings
  //  "-Ywarn-adapted-args", // Warn if an argument list is modified to match the receiver
  "-Ywarn-value-discard", // Warn when non-Unit expression results are unused
  //  "-Ywarn-inaccessible",
  "-Ywarn-dead-code",
  "-language:reflectiveCalls",
  "-Ydelambdafy:method"
)

val quillVersion = "3.2.2"

val `com.h2database_h2` = "com.h2database" % "h2" % "1.4.192"

val `com.typesafe.scala-logging_scala-logging` = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"

val `ch.qos.logback_logback-classic` = "ch.qos.logback" % "logback-classic" % "1.2.3"

val `io.getquill_quill-async` = "io.getquill" %% "quill-async" % quillVersion

val `io.getquill_quill-async-mysql` = "io.getquill" %% "quill-async-mysql" % quillVersion

val `io.getquill_quill-jdbc` = "io.getquill" %% "quill-jdbc" % quillVersion

val `org.scalatest_scalatest` = "org.scalatest" %% "scalatest" % "3.0.8" % Test

val `org.scalacheck_scalacheck` = "org.scalacheck" %% "scalacheck" % "1.14.0" % Test


lazy val `macro-quill` = projectWithName("macro-quill", file(".")).settings(
  libraryDependencies ++= Seq(
    `io.getquill_quill-async`,
    `ch.qos.logback_logback-classic`,
    `com.typesafe.scala-logging_scala-logging`,
    `io.getquill_quill-async-mysql` % Test,
    `io.getquill_quill-jdbc` % Test,
    `com.h2database_h2` % Test
  )
)




def projectWithName(name: String, file: File): Project = Project(name, file).settings(
  libraryDependencies ++= Seq(
    `org.scalatest_scalatest`,
    `org.scalacheck_scalacheck`
  ),
  licenseReportTitle := "Copyright (c) 2019 Andrzej Jozwik",
  licenseSelection := Seq(LicenseCategory.MIT),
  sources in(Compile, doc) := Seq.empty,
  scalariformPreferences := scalariformPreferences.value
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(DoubleIndentConstructorArguments, true)
    .setPreference(DanglingCloseParenthesis, Preserve)
)


