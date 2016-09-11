import com.typesafe.sbt.packager.MappingsHelper._




name := "Tu1"

version := "1.0"

lazy val `tu1` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq( jdbc , cache , ws   , specs2 % Test )


libraryDependencies ++= Seq(
  ws
)

libraryDependencies +=   "com.typesafe.play" %% "play-mailer" % "5.0.0"



mappings in Universal ++= directory(baseDirectory.value / "imageDataset")

mappings in Universal ++= directory(baseDirectory.value / "libs")

routesGenerator := StaticRoutesGenerator