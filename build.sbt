name := "LuciusCore"

version := "3.2.9"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
  //(
  "org.scalactic"      %% "scalactic"       % "3.0.7"      % "test",
  //)
    /* .exclude("org.scala-lang", "scala-reflect"), */
  "org.scalatest"      %% "scalatest"       % "3.0.7"      % "test",
  "org.apache.spark"   %% "spark-core"      % "2.4.0"      % "provided"
)

scalacOptions ++= Seq("-unchecked", "-deprecation")

organization := "com.data-intuitive"

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

githubOwner := "data-intuitive"
githubRepository := "luciuscore"
githubTokenSource := TokenSource.GitConfig("github.token")
publishMavenStyle := true
publishConfiguration := publishConfiguration.value.withOverwrite(true)

/* bintrayPackageLabels := Seq("scala", "l1000", "spark", "lucius") */

