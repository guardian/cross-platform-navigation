
name:="cross-platform-navigation"
version:="1.0"

scalaVersion := "2.12"
crossScalaVersions := Seq("2.11.11", scalaVersion.value)

libraryDependencies ++= Seq(
  "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided"
)




