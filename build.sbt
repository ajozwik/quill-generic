import com.sksamuel.scapegoat.sbt.ScapegoatSbtPlugin.autoImport._

val `scalaVersion_2.13` = "2.13.3"

val `scalaVersion_2.12` = "2.12.11"

val `only2_12` = Seq(`scalaVersion_2.12`)

val targetJdk = "1.8"

ThisBuild / scalacOptions ++= Seq("-Dquill.macro.log=false")

ThisBuild / scapegoatVersion := {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, n)) if n >= 13 => "1.4.5"
    case _                       => "1.3.11"
  }
}

//ThisBuild / turbo := true

resolvers += Resolver.sonatypeRepo("releases")

lazy val scalaVersionFromProps = sys.props.getOrElse("macro.scala.version", `scalaVersion_2.12`)

ThisBuild / scalaVersion := {
  if (is213Version(scalaVersionFromProps))
    `scalaVersion_2.13`
  else
    `scalaVersion_2.12`
}

ThisBuild / crossScalaVersions := Set(scalaVersion.value, `scalaVersion_2.12`).toSeq

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

val quillVersion = "3.5.1"

val `com.h2database_h2` = "com.h2database" % "h2" % "1.4.200"

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

val `org.scalatest_scalatest` = "org.scalatest" %% "scalatest" % "3.1.2" % Test

val `org.scalacheck_scalacheck` = "org.scalacheck" %% "scalacheck" % "1.14.3" % Test

val `org.scalatestplus_scalacheck-1-14` = "org.scalatestplus" %% "scalacheck-1-14" % "3.1.1.1" % "test"

val `org.cassandraunit_cassandra-unit` = "org.cassandraunit" % "cassandra-unit" % "3.11.2.0"

val `com.datastax.cassandra_cassandra-driver-extras` = "com.datastax.cassandra" % "cassandra-driver-extras" % "3.8.0"

def is213Version(version: String): Boolean = version.startsWith("2.13")

def modulesFromProps: Seq[ClasspathDep[ProjectReference]] =
  if (is213Version(scalaVersionFromProps))
    scala213Modules
  else
    allModules

lazy val `quill-macro-parent` =
  (project in file("."))
    .settings(skip in publish := true)
    .aggregate(modulesFromProps.map(_.project): _*)
    .dependsOn(modulesFromProps: _*)

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

lazy val `quill-cassandra-monix-macro` = projectWithNameOnly12("quill-cassandra-monix-macro", file("quill-cassandra-monix-macro"))
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

lazy val `quill-cassandra-macro` = projectWithNameOnly12("quill-cassandra-macro", file("quill-cassandra-macro"))
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

lazy val `quill-async-jdbc-macro` = projectWithNameOnly12("quill-async-jdbc-macro", file("quill-async-jdbc-macro"))
  .settings(libraryDependencies ++= Seq(`io.getquill_quill-async`, `io.getquill_quill-async-mysql` % Test))
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
  baseModules ++ dbModules

lazy val allModules =
  scala213Modules ++ asyncDbModules ++ cassandraModules

def projectWithNameOnly12(name: String, file: File): Project =
  projectWithName(name, file).settings(
    crossScalaVersions := `only2_12`,
    skip in publish := is213Version(scalaVersion.value)
  )

def projectWithName(name: String, file: File): Project =
  Project(name, file).settings(
    libraryDependencies ++= Seq(
          `org.scalatest_scalatest`,
          `org.scalacheck_scalacheck`,
          `org.scalatestplus_scalacheck-1-14`
        ),
    licenseReportTitle := s"Copyright (c) ${java.time.LocalDate.now.getYear} Andrzej Jozwik",
    licenseSelection := Seq(LicenseCategory.MIT),
    sources in (Compile, doc) := Seq.empty
  )
