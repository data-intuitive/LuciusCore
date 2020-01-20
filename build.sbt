name := "LuciusCore"

version := "3.2.12"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
  "org.scalactic"      %% "scalactic"       % "3.0.7"      % "test",
  "org.scalatest"      %% "scalatest"       % "3.0.7"      % "test",
  "org.apache.spark"   %% "spark-core"      % "2.4.0"      % "provided"
)

scalacOptions ++= Seq("-unchecked", "-deprecation")

organization := "com.data-intuitive"

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

/* bintrayPackageLabels := Seq("scala", "l1000", "spark", "lucius") */

