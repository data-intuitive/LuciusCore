name := "LuciusCore"

version := "4.0.0-SNAPSHOT"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
  "org.scalaz"         %% "scalaz-core"     % "7.3.3",
  "org.scalactic"      %% "scalactic"       % "3.2.2"      % "test",
  "org.scalatest"      %% "scalatest"       % "3.2.2"      % "test",
  "org.scalatest"      %% "scalatest-shouldmatchers"       % "3.2.2"      % "test",
  "org.apache.spark"   %% "spark-core"      % "2.4.7"      % "provided"
)

scalacOptions ++= Seq("-unchecked", "-deprecation")

organization := "com.data-intuitive"

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

/* githubOwner := "data-intuitive" */
/* githubRepository := "luciuscore" */
/* githubActor := "tverbeiren" */
/* githubTokenSource := TokenSource.GitConfig("github.token") */
/* publishMavenStyle := true */
/* publishConfiguration := publishConfiguration.value.withOverwrite(true) */

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))
bintrayPackageLabels := Seq("scala", "l1000", "spark", "lucius", "compass")
