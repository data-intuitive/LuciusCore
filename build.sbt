name := "LuciusCore"

version := "3.0.0"

scalaVersion := "2.11.8"

// crossScalaVersions := Seq("2.10.6", "2.11.8")

libraryDependencies ++= Seq(
  "org.scalactic"      %% "scalactic"       % "3.0.0"                  ,
  "org.scalatest"      %% "scalatest"       % "3.0.0"      % "test"    ,
  "org.apache.spark"   %% "spark-core"      % "2.0.1"      % "provided",
  "org.apache.spark"  %% "spark-sql" % "2.0.1" % "provided"
)

scalacOptions ++= Seq("-unchecked", "-deprecation")

organization := "com.data-intuitive"
licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))
bintrayPackageLabels := Seq("scala", "l1000", "spark", "lucius")
