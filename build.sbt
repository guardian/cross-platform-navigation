
name:="cross-platform-navigation"
version:="1.0"

scalaVersion in ThisBuild := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.6.6",
  "com.lihaoyi" %% "pprint" % "0.5.3",
  "com.amazonaws" % "aws-java-sdk-s3" % "1.11.241",
  "com.amazonaws" % "aws-java-sdk-ec2" % "1.11.241",
  "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided",
  "io.spray" %% "spray-caching" % "1.3.4",
  "org.slf4j" % "slf4j-api" % "1.7.25",
  "org.specs2" %% "specs2-core" % "3.8.6" % "test"
)

unmanagedResourceDirectories in Test += baseDirectory.value / "json"




