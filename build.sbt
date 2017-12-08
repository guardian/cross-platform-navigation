import sbtrelease.ReleaseStateTransformations._

name:="cross-platform-navigation"
version:="1.0"

scalaVersion in ThisBuild := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.6.6",
  "org.slf4j" % "slf4j-api" % "1.7.25",
  "org.specs2" %% "specs2-core" % "3.8.6" % "test"
)

unmanagedResourceDirectories in Test += baseDirectory.value / "json"

enablePlugins(RiffRaffArtifact, BuildInfoPlugin)

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

riffRaffPackageType := file(".nope")
riffRaffUploadArtifactBucket := Option("riffraff-artifact")
riffRaffUploadManifestBucket := Option("riffraff-builds")
riffRaffManifestProjectName := s"Mobile::${name.value}"
riffRaffArtifactResources ++= listJsonFilesInJsonDir

publishMavenStyle := true
publishArtifact in Test := false
pomIncludeRepository := {_ => false}
description := "Provides scala representation of the navigation menus for the www.theguardian.com and guardian apps"

//TODO scala version, cross scala version

//PgpPublis
organization := "com.gu"
licenses := Seq("Apache v2" -> url("http://www.apache.org/licenses/LICENSE-2.0.html"))
scmInfo := Some(ScmInfo(
  url("https://github.com/guardian/cross-platform-navigation"),
  "scm:git:git@github.com:guardian/cross-platform-navigation.git"
))
javacOptions ++= Seq("-source", "1.7", "-target", "1.7")
scalacOptions ++= Seq("-deprecation", "-unchecked")
releaseProcess := Seq(
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  setNextVersion,
  commitNextVersion,
  releaseStepCommand("sonatypeReleaseAll"),
  pushChanges
)
