val `scalaVersion_2.13` = "2.13.12"

val `scalaVersion_2.12` = "2.12.17"

ThisBuild / scalaVersion := `scalaVersion_2.13`

val targetJdk = "11"

ThisBuild / scalacOptions ++= Seq("-Dquill.macro.log=false", "-language:higherKinds")

def init(): Unit = {
  sys.props.put("quill.macro.log", false.toString)
  sys.props.put("quill.binds.log", true.toString)
}

val fake: Unit = init()

resolvers ++= Resolver.sonatypeOssRepos("releases")

ThisBuild / libraryDependencySchemes += "org.typelevel" %% "cats-effect" % "always"

ThisBuild / crossScalaVersions := Seq(`scalaVersion_2.13`, `scalaVersion_2.12`)

ThisBuild / Test / fork := true

ThisBuild / organization := "com.github.ajozwik"

ThisBuild / scalacOptions ++= Seq(
  "-encoding",
  "UTF-8",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Xlint",
  "-Ywarn-value-discard",
  "-Ywarn-dead-code",
  "-language:reflectiveCalls",
  "-Ydelambdafy:method",
  "-Xsource:3",
  "-language:implicitConversions"
) ++ {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, n)) if n <= 12 =>
      Seq(s"-target:jvm-$targetJdk")
    case _ =>
      Seq(s"-release:$targetJdk")
  }
}

ThisBuild / javacOptions ++= Seq("-Xlint:deprecation", "-Xdiags:verbose", "-source", targetJdk, "-target", targetJdk)

val quillVersion          = scala.util.Properties.propOrElse("quill.version", "4.8.1")
val quillCassandraVersion = quillVersion

val scalaTestVersion = "3.2.17"

val `ch.qos.logback_logback-classic`                 = "ch.qos.logback"              % "logback-classic"         % "1.3.14"
val `com.datastax.cassandra_cassandra-driver-extras` = "com.datastax.cassandra"      % "cassandra-driver-extras" % "3.11.5"
val `com.h2database_h2`                              = "com.h2database"              % "h2"                      % "2.2.224"
val `com.typesafe.scala-logging_scala-logging`       = "com.typesafe.scala-logging" %% "scala-logging"           % "3.9.5"
val `dev.zio_zio-interop-cats`                       = "dev.zio"                    %% "zio-interop-cats"        % "23.1.0.0"
val `io.getquill_quill-cassandra-monix`              = "io.getquill"                %% "quill-cassandra-monix"   % quillCassandraVersion
val `io.getquill_quill-cassandra`                    = "io.getquill"                %% "quill-cassandra"         % quillCassandraVersion
val `io.getquill_quill-doobie`                       = "io.getquill"                %% "quill-doobie"            % quillVersion
val `io.getquill_quill-jdbc-monix`                   = "io.getquill"                %% "quill-jdbc-monix"        % quillVersion
val `io.getquill_quill-jdbc-zio`                     = "io.getquill"                %% "quill-jdbc-zio"          % quillVersion
val `io.getquill_quill-jdbc`                         = "io.getquill"                %% "quill-jdbc"              % quillVersion
val `io.getquill_quill-sql`                          = "io.getquill"                %% "quill-sql"               % quillVersion
val `org.cassandraunit_cassandra-unit`               = "org.cassandraunit"           % "cassandra-unit"          % "4.3.1.0"
val `org.scalacheck_scalacheck`                      = "org.scalacheck"             %% "scalacheck"              % "1.17.0"               % Test
val `org.scalatest_scalatest`                        = "org.scalatest"              %% "scalatest"               % scalaTestVersion       % Test
val `org.scalatestplus_scalacheck`                   = "org.scalatestplus"          %% "scalacheck-1-17"         % s"$scalaTestVersion.0" % Test
val `org.tpolecat_doobie-h2`                         = "org.tpolecat"               %% "doobie-h2"               % "1.0.0-RC4"
val `org.typelevel_cats-core`                        = "org.typelevel"              %% "cats-core"               % "2.10.0"
val `org.typelevel_cats-effect`                      = "org.typelevel"              %% "cats-effect"             % "3.5.0"

def is213Version(version: String): Boolean = version.startsWith("2.13")

publish / skip := true

lazy val `repository` = projectWithName("repository", file("repository")).settings(
  libraryDependencies ++= Seq(
    `io.getquill_quill-sql`,
    `ch.qos.logback_logback-classic`           % Test,
    `com.typesafe.scala-logging_scala-logging` % Test,
    `com.h2database_h2`                        % Test
  )
)

lazy val `repository-monad` = projectWithName("repository-monad", file("repository-monad"))
  .settings(
    libraryDependencies ++= Seq(`org.typelevel_cats-core`)
  )
  .dependsOn(`repository`)
  .dependsOn(`repository` % "test->test")

lazy val `repository-doobie` = projectWithName("repository-doobie", file("repository-doobie"))
  .settings(libraryDependencies ++= Seq(`io.getquill_quill-doobie`, `org.tpolecat_doobie-h2` % Test))
  .dependsOn(`repository-jdbc-monad`)
  .dependsOn(`repository` % "test->test")

lazy val `quill-jdbc-monix` = projectWithName("quill-jdbc-monix", file("quill-jdbc-monix"))
  .settings(libraryDependencies ++= Seq(`io.getquill_quill-jdbc-monix`))
  .dependsOn(`repository-monad`)
  .dependsOn(Seq(`repository`).map(_ % "test->test")*)

lazy val `quill-jdbc-zio` = projectWithName("quill-jdbc-zio", file("quill-jdbc-zio"))
  .settings(libraryDependencies ++= Seq(`io.getquill_quill-jdbc-zio`, `dev.zio_zio-interop-cats`, `org.typelevel_cats-effect`))
  .dependsOn(`repository-monad`)
  .dependsOn(Seq(`repository`, `repository-jdbc-monad`).map(_ % "test->test")*)

lazy val `quill-cassandra-monix` = projectWithName("quill-cassandra-monix", file("quill-cassandra-monix"))
  .settings(
    libraryDependencies ++= Seq(
      `io.getquill_quill-cassandra-monix`,
      `org.cassandraunit_cassandra-unit`               % Test,
      `com.datastax.cassandra_cassandra-driver-extras` % Test
    ),
    Test / fork := true
  )
  .dependsOn(`repository-monad`, `repository-cassandra`)
  .dependsOn(Seq(`repository`, `repository-cassandra`).map(_ % "test->test")*)

lazy val `repository-cassandra` = projectWithName("repository-cassandra", file("repository-cassandra"))
  .settings(
    libraryDependencies ++= Seq(
      `io.getquill_quill-cassandra`,
      `org.cassandraunit_cassandra-unit`               % Test,
      `com.datastax.cassandra_cassandra-driver-extras` % Test
    ),
    Test / fork := true
  )
  .dependsOn(`repository-monad`)
  .dependsOn(Seq(`repository`).map(_ % "test->test")*)

lazy val `repository-jdbc-monad` = projectWithName("repository-jdbc-monad", file("repository-jdbc-monad"))
  .settings(libraryDependencies ++= Seq(`io.getquill_quill-jdbc`))
  .dependsOn(`repository-monad`, `repository` % "test->test")

lazy val baseModules =
  Seq[sbt.ClasspathDep[sbt.ProjectReference]](`repository`)

lazy val dbModules =
  Seq[sbt.ClasspathDep[sbt.ProjectReference]](`repository-jdbc-monad`, `quill-jdbc-monix`)

lazy val cassandraModules =
  Seq[sbt.ClasspathDep[sbt.ProjectReference]](`repository-cassandra`, `quill-cassandra-monix`)

lazy val scala213Modules =
  baseModules ++ dbModules

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
