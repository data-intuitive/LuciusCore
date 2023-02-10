name := "LuciusCore"

version in ThisBuild := "4.1.2"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
  "org.scalaz"         %% "scalaz-core"     % "7.3.3",
  "org.scalactic"      %% "scalactic"       % "3.2.2"      % "test",
  "org.scalatest"      %% "scalatest"       % "3.2.2"      % "test",
  "org.scalatest"      %% "scalatest-shouldmatchers"       % "3.2.2"      % "test",
  "org.apache.spark"   %% "spark-core"      % "2.4.7"      % "provided",
  "org.apache.spark"   %% "spark-sql"       % "2.4.7"      % "provided"
)

scalacOptions ++= Seq("-unchecked", "-deprecation")

organization := "com.data-intuitive"

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

// publish to github packages
publishTo := Some("GitHub data-intuitive Apache Maven Packages" at "https://maven.pkg.github.com/data-intuitive/luciuscore")
publishMavenStyle := true
credentials += Credentials(
  "GitHub Package Registry",
  "maven.pkg.github.com",
  "tverbeiren",
  System.getenv("GITHUB_TOKEN")
)

publishArtifact in (Compile, packageDoc) := false

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))
