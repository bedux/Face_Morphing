name := "Tu1"

version := "1.0"

lazy val `tu1` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq( jdbc , cache , ws   , specs2 % Test )


libraryDependencies ++= Seq(
  ws
)