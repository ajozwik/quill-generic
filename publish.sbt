ThisBuild / developers := List(
  Developer(
    id = "ajozwik",
    name = "Andrzej Jozwik",
    email = "andrzej.jozwik@gmail.com",
    url = url("https://github.com/ajozwik")
  )
)

ThisBuild / publishTo := sonatypePublishTo.value

Test / publishArtifact := false

ThisBuild / licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php"))

val organizationUrl = "https://github.com/ajozwik"

val projectUrl = s"$organizationUrl/quill-generic"

ThisBuild / scmInfo := Option(
  ScmInfo(
    url(projectUrl),
    "scm:git@github.com:ajozwik/quill-generic.git"
  )
)

ThisBuild / organizationHomepage := Option(url(organizationUrl))

ThisBuild / homepage := Option(url(projectUrl))

ThisBuild / versionScheme := Option("semver-spec")
