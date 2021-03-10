val `scalaVersion_2.13` = "2.13.5"

val `scalaVersion_2.12` = "2.12.12"

ThisBuild / scalaVersion := `scalaVersion_2.12`

val targetJdk = "1.8"

ThisBuild / scalacOptions ++= Seq("-Dquill.macro.log=false")

//ThisBuild / turbo := true

resolvers += Resolver.sonatypeRepo("releases")

ThisBuild / crossScalaVersions := Seq(`scalaVersion_2.13`, `scalaVersion_2.12`)

ThisBuild / fork in Test := true

ThisBuild / organization := "com.github.ajozwik"

ThisBuild / scalacOptions ++= Seq(
  "-encoding",
  "UTF-8",
  "-deprecation", // warning and location for usages of deprecated APIs
  "-feature",     // warning and location for usages of features that should be imported explicitly
  "-unchecked",   // additional warnings where generated code depends on assumptions
  "-Xlint",       // recommended additional warnings
  //  "-Ywarn-adapted-args", // Warn if an argument list is modified to match the receiver
  "-Ywarn-value-discard", // Warn when non-Unit expression results are unused
  //  "-Ywarn-inaccessible",
  "-Ywarn-dead-code",
  "-language:reflectiveCalls",
  "-Ydelambdafy:method",
  s"-target:jvm-$targetJdk"
)

ThisBuild / javacOptions ++= Seq("-Xlint:deprecation", "-Xdiags:verbose", "-source", targetJdk, "-target", targetJdk)

val quillVersion = scala.util.Properties.propOrElse("quill.version", "3.6.1")

val scalaTestVersion = "3.2.6"

val `com.h2database_h2` = "com.h2database" % "h2" % "1.4.200"

val `com.typesafe.scala-logging_scala-logging` = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"

val `ch.qos.logback_logback-classic` = "ch.qos.logback" % "logback-classic" % "1.2.3"

val `io.getquill_quill-core` = "io.getquill" %% "quill-core" % quillVersion

val `io.getquill_quill-jasync` = "io.getquill" %% "quill-jasync" % quillVersion

val `io.getquill_quill-jasync-mysql` = "io.getquill" %% "quill-jasync-mysql" % quillVersion

val `io.getquill_quill-cassandra-monix` = "io.getquill" %% "quill-cassandra-monix" % quillVersion

val `io.getquill_quill-cassandra` = "io.getquill" %% "quill-cassandra" % quillVersion

val `io.getquill_quill-jdbc` = "io.getquill" %% "quill-jdbc" % quillVersion

val `io.getquill_quill-jdbc-monix` = "io.getquill" %% "quill-jdbc-monix" % quillVersion

val `io.getquill_quill-monix` = "io.getquill" %% "quill-monix" % quillVersion

val `org.scalatest_scalatest` = "org.scalatest" %% "scalatest" % scalaTestVersion % Test

val `org.scalacheck_scalacheck` = "org.scalacheck" %% "scalacheck" % "1.15.3" % Test

val `org.scalatestplus_scalacheck-1-15` = "org.scalatestplus" %% "scalacheck-1-15" % s"$scalaTestVersion.0" % Test

val `org.cassandraunit_cassandra-unit` = "org.cassandraunit" % "cassandra-unit" % "3.11.2.0"

val `com.datastax.cassandra_cassandra-driver-extras` = "com.datastax.cassandra" % "cassandra-driver-extras" % "3.10.2"

def is213Version(version: String): Boolean = version.startsWith("2.13")

skip in publish := true

lazy val `macro-quill` = projectWithName("macro-quill", file("macro-quill")).settings(
  libraryDependencies ++= Seq(
    `io.getquill_quill-core`,
    `ch.qos.logback_logback-classic`           % Test,
    `com.typesafe.scala-logging_scala-logging` % Test,
    `com.h2database_h2`                        % Test
  )
)

lazy val `quill-jdbc-monix-macro` = projectWithName("quill-jdbc-monix-macro", file("quill-jdbc-monix-macro"))
  .settings(libraryDependencies ++= Seq(`io.getquill_quill-jdbc-monix`))
  .dependsOn(`quill-monix-macro`)
  .dependsOn(Seq(`macro-quill`, `quill-monix-macro`).map(_ % "test->test"): _*)

lazy val `quill-monix-macro` = projectWithName("quill-monix-macro", file("quill-monix-macro"))
  .settings(libraryDependencies ++= Seq(`io.getquill_quill-monix`))
  .dependsOn(`macro-quill`, `macro-quill` % "test->test")

lazy val `quill-cassandra-monix-macro` = projectWithName("quill-cassandra-monix-macro", file("quill-cassandra-monix-macro"))
  .settings(
    libraryDependencies ++= Seq(
      `io.getquill_quill-cassandra-monix`,
      `org.cassandraunit_cassandra-unit`               % Test,
      `com.datastax.cassandra_cassandra-driver-extras` % Test
    ),
    Test / fork := true
  )
  .dependsOn(`quill-monix-macro`)
  .dependsOn(Seq(`macro-quill`, `quill-monix-macro`, `quill-cassandra-macro`).map(_ % "test->test"): _*)

lazy val `quill-cassandra-macro` = projectWithName("quill-cassandra-macro", file("quill-cassandra-macro"))
  .settings(
    libraryDependencies ++= Seq(
      `io.getquill_quill-cassandra`,
      `org.cassandraunit_cassandra-unit`               % Test,
      `com.datastax.cassandra_cassandra-driver-extras` % Test
    ),
    Test / fork := true
  )
  .dependsOn(`macro-quill`)
  .dependsOn(Seq(`macro-quill`).map(_ % "test->test"): _*)

lazy val `quill-jdbc-macro` = projectWithName("quill-jdbc-macro", file("quill-jdbc-macro"))
  .settings(libraryDependencies ++= Seq(`io.getquill_quill-jdbc`))
  .dependsOn(`macro-quill`, `macro-quill` % "test->test")

lazy val `quill-async-jdbc-macro` = projectWithName("quill-async-jdbc-macro", file("quill-async-jdbc-macro"))
  .settings(libraryDependencies ++= Seq(`io.getquill_quill-jasync`, `io.getquill_quill-jasync-mysql` % Test))
  .dependsOn(`macro-quill`, `macro-quill` % "test->test")

lazy val baseModules =
  Seq[sbt.ClasspathDep[sbt.ProjectReference]](`macro-quill`)

lazy val dbModules =
  Seq[sbt.ClasspathDep[sbt.ProjectReference]](`quill-jdbc-macro`, `quill-jdbc-monix-macro`, `quill-monix-macro`)

lazy val asyncDbModules =
  Seq[sbt.ClasspathDep[sbt.ProjectReference]](`quill-async-jdbc-macro`)

lazy val cassandraModules =
  Seq[sbt.ClasspathDep[sbt.ProjectReference]](`quill-cassandra-macro`, `quill-cassandra-monix-macro`)

lazy val scala213Modules =
  baseModules ++ dbModules ++ asyncDbModules

lazy val allModules =
  scala213Modules ++ cassandraModules

def projectWithName(name: String, file: File): Project =
  Project(name, file).settings(
    libraryDependencies ++= Seq(
      `org.scalatest_scalatest`,
      `org.scalacheck_scalacheck`,
      `org.scalatestplus_scalacheck-1-15`
    ),
    licenseReportTitle := s"Copyright (c) ${java.time.LocalDate.now.getYear} Andrzej Jozwik",
    licenseSelection := Seq(LicenseCategory.MIT),
    sources in (Compile, doc) := Seq.empty,
    wartremoverWarnings in (Compile, compile) ++= Warts.all.filterNot(Set(Wart.ImplicitParameter, Wart.DefaultArguments).contains),
    coverageScalacPluginVersion := "1.4.2"
  )
