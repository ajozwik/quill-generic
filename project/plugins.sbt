logLevel := Level.Warn

addSbtPlugin("org.wartremover" % "sbt-wartremover" % "2.4.13")

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.2")

addSbtPlugin("com.sksamuel.scapegoat" %% "sbt-scapegoat" % "1.1.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-license-report" % "1.2.0")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.6.1")

addSbtPlugin("org.scoverage" % "sbt-coveralls" % "1.2.7")

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.9.5")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "2.1.1")
