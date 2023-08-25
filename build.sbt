import sbtrelease.ReleaseStateTransformations._

name:="cross-platform-navigation"

ThisBuild / scalaVersion := "2.13.11"

crossScalaVersions := Seq(scalaVersion.value, "2.12.18")

resolvers ++= Resolver.sonatypeOssRepos("releases")

libraryDependencies ++= Seq(
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.15.2",
  "com.typesafe.play" %% "play-json" % "2.9.4",
  "org.slf4j" % "slf4j-api" % "2.0.7",
  "org.specs2" %% "specs2-core" % "4.20.0" % Test
)

Test / unmanagedResourceDirectories += baseDirectory.value / "json"

enablePlugins(BuildInfoPlugin)

def listJsonFiles(file: File) : List[File] = {
  if(file.isDirectory) {
    file.listFiles().toList.flatMap(listJsonFiles)
  } else {
    List(file)
  }
}

def listJsonFilesInJsonDir: List[(File, String)] = {

  val jsonFilesDir = file("json")
  val jsonDir = jsonFilesDir.getAbsoluteFile.toPath.getParent

  listJsonFiles(jsonFilesDir).map {
    file => file -> jsonDir.relativize(file.getAbsoluteFile.toPath).toString
  }
}

publishTo := sonatypePublishToBundle.value
publishMavenStyle := true
Test / publishArtifact := false
pomIncludeRepository := {_ => false}
description := "Provides scala representation of the navigation menus for the www.theguardian.com and guardian apps"

pomExtra in Global := {
  <url>https://github.com/guardian/cross-platform-navigation</url>
    <developers>
      <developer>
        <id>@guardian</id>
        <name>The guardian</name>
        <url>https://github.com/guardian</url>
      </developer>
    </developers>
}


//PgpPublis
organization := "com.gu"
licenses := Seq("Apache v2" -> url("http://www.apache.org/licenses/LICENSE-2.0.html"))
scmInfo := Some(ScmInfo(
  url("https://github.com/guardian/cross-platform-navigation"),
  "scm:git:git@github.com:guardian/cross-platform-navigation.git"
))
scalacOptions ++= Seq("-deprecation", "-unchecked", "-release:11")
releaseCrossBuild := true
releaseProcess := Seq(
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseStepCommandAndRemaining("+publishSigned"),
  releaseStepCommand("sonatypeBundleRelease"),
  setNextVersion,
  commitNextVersion,
  pushChanges
)

Test/testOptions += Tests.Argument(
  TestFrameworks.ScalaTest,
  "-u", s"test-results/scala-${scalaVersion.value}", "-o"
)
