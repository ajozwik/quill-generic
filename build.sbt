val `scalaVersion_2.13` = "2.13.10"

val `scalaVersion_2.12` = "2.12.17"

//val `scalaVersion_3` = "3.2.0"

ThisBuild / scalaVersion := `scalaVersion_2.13`

val targetJdk = "8"

ThisBuild / scalacOptions ++= Seq("-Dquill.macro.log=false", "-language:higherKinds")

def init(): Unit = {
  sys.props.put("quill.macro.log", false.toString)
  sys.props.put("quill.binds.log", true.toString)
}

val fake = init()

resolvers ++= Resolver.sonatypeOssRepos("releases")

ThisBuild / crossScalaVersions := Seq(`scalaVersion_2.13`, `scalaVersion_2.12`)

ThisBuild / Test / fork := true

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
  "-Ydelambdafy:method"
) ++ {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, n)) if n <= 12 =>
      Seq(s"-target:jvm-$targetJdk")
    case _ =>
      Seq(s"-release:$targetJdk")
  }
}

ThisBuild / javacOptions ++= Seq("-Xlint:deprecation", "-Xdiags:verbose", "-source", targetJdk, "-target", targetJdk)

val quillVersion = scala.util.Properties.propOrElse("quill.version", "4.6.0")

val scalaTestVersion = "3.2.15"

val `com.h2database_h2` = "com.h2database" % "h2" % "1.4.200"

val `com.typesafe.scala-logging_scala-logging` = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5"

val `ch.qos.logback_logback-classic` = "ch.qos.logback" % "logback-classic" % "1.2.11"

//val `io.getquill_quill-core` = "io.getquill" %% "quill-core" % quillVersion

val `io.getquill_quill-sql` = "io.getquill" %% "quill-sql" % quillVersion

val `io.getquill_quill-jasync` = "io.getquill" %% "quill-jasync" % quillVersion

val `io.getquill_quill-jasync-mysql` = "io.getquill" %% "quill-jasync-mysql" % quillVersion

val `io.getquill_quill-cassandra-monix` = "io.getquill" %% "quill-cassandra-monix" % quillVersion

val `io.getquill_quill-cassandra` = "io.getquill" %% "quill-cassandra" % quillVersion

val `io.getquill_quill-jdbc` = "io.getquill" %% "quill-jdbc" % quillVersion

val `io.getquill_quill-jdbc-monix` = "io.getquill" %% "quill-jdbc-monix" % quillVersion

val `io.getquill_quill-monix` = "io.getquill" %% "quill-monix" % quillVersion

val `org.scalatest_scalatest` = "org.scalatest" %% "scalatest" % scalaTestVersion % Test

val `org.scalacheck_scalacheck` = "org.scalacheck" %% "scalacheck" % "1.17.0" % Test

val `org.scalatestplus_scalacheck` = "org.scalatestplus" %% "scalacheck-1-17" % s"$scalaTestVersion.0" % Test

val `org.cassandraunit_cassandra-unit` = "org.cassandraunit" % "cassandra-unit" % "4.3.1.0"

val `com.datastax.cassandra_cassandra-driver-extras` = "com.datastax.cassandra" % "cassandra-driver-extras" % "3.11.3"

def is213Version(version: String): Boolean = version.startsWith("2.13")

publish / skip := true

lazy val `macro-quill` = projectWithName("macro-quill", file("macro-quill")).settings(
  libraryDependencies ++= Seq(
    `io.getquill_quill-sql`,
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
      `org.scalatestplus_scalacheck`
    ),
    licenseReportTitle      := s"Copyright (c) ${java.time.LocalDate.now.getYear} Andrzej Jozwik",
    licenseSelection        := Seq(LicenseCategory.MIT),
    Compile / doc / sources := Seq.empty,
    Compile / compile / wartremoverWarnings ++= Warts.allBut(Wart.ImplicitParameter, Wart.DefaultArguments)
  )
