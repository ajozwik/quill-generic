import com.sksamuel.scapegoat.sbt.ScapegoatSbtPlugin.autoImport._

val `scalaVersion_2.13` = "2.13.0"

val `scalaVersion_2.12` = "2.12.10"

name := "quill-macro-parent"

ThisBuild / scalacOptions ++= Seq("-Dquill.macro.log=false")

ThisBuild / scapegoatVersion := "1.3.10"

//ThisBuild / turbo := true

resolvers += Resolver.sonatypeRepo("releases")

val supportedScalaVersions = Seq(`scalaVersion_2.13`, `scalaVersion_2.12`)

ThisBuild / crossScalaVersions := supportedScalaVersions

ThisBuild / scalaVersion := `scalaVersion_2.12`

ThisBuild / organization := "com.github.ajozwik"

ThisBuild / scalacOptions ++= Seq(
  "-target:jvm-1.8",
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
  "-Ydelambdafy:method"
)

val quillVersion = "3.4.7"

val `com.h2database_h2` = "com.h2database" % "h2" % "1.4.199"

val `com.typesafe.scala-logging_scala-logging` = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"

val `ch.qos.logback_logback-classic` = "ch.qos.logback" % "logback-classic" % "1.2.3"

val `io.getquill_quill-core` = "io.getquill" %% "quill-core" % quillVersion

val `io.getquill_quill-async` = "io.getquill" %% "quill-async" % quillVersion

val `io.getquill_quill-async-mysql` = "io.getquill" %% "quill-async-mysql" % quillVersion

val `io.getquill_quill-cassandra-monix` = "io.getquill" %% "quill-cassandra-monix" % quillVersion

val `io.getquill_quill-cassandra` = "io.getquill" %% "quill-cassandra" % quillVersion

val `io.getquill_quill-jdbc` = "io.getquill" %% "quill-jdbc" % quillVersion

val `io.getquill_quill-jdbc-monix` = "io.getquill" %% "quill-jdbc-monix" % quillVersion

val `io.getquill_quill-monix` = "io.getquill" %% "quill-monix" % quillVersion

val `org.scalatest_scalatest` = "org.scalatest" %% "scalatest" % "3.0.8" % Test

val `org.scalacheck_scalacheck` = "org.scalacheck" %% "scalacheck" % "1.14.0" % Test

val `org.cassandraunit_cassandra-unit` = "org.cassandraunit" % "cassandra-unit" % "3.11.2.0"

val `com.datastax.cassandra_cassandra-driver-extras` = "com.datastax.cassandra" % "cassandra-driver-extras" % "3.7.2"

lazy val `macro-quill` = projectWithName("macro-quill", file("macro-quill")).settings(
  libraryDependencies ++= Seq(
        `io.getquill_quill-core`,
        `ch.qos.logback_logback-classic`           % Test,
        `com.typesafe.scala-logging_scala-logging` % Test,
        `com.h2database_h2`                        % Test
      ),
  scapegoatIgnoredFiles := Seq(".*/*Macro.*.scala", ".*/.*Queries.*.scala")
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
  .settings(libraryDependencies ++= Seq(`io.getquill_quill-jdbc`, `io.getquill_quill-async-mysql` % Test))
  .dependsOn(`macro-quill`, `macro-quill` % "test->test")

lazy val `quill-async-jdbc-macro` = projectWithName("quill-async-jdbc-macro", file("quill-async-jdbc-macro"))
  .settings(libraryDependencies ++= Seq(`io.getquill_quill-async`, `io.getquill_quill-async-mysql` % Test))
  .dependsOn(`macro-quill`, `macro-quill` % "test->test")

def projectWithName(name: String, file: File): Project = Project(name, file).settings(
  libraryDependencies ++= Seq(
        `org.scalatest_scalatest`,
        `org.scalacheck_scalacheck`
      ),
  licenseReportTitle := "Copyright (c) 2019 Andrzej Jozwik",
  licenseSelection := Seq(LicenseCategory.MIT),
  sources in (Compile, doc) := Seq.empty
)
