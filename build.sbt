import sbtrelease.ReleaseStateTransformations._

name:="cross-platform-navigation"

ThisBuild / scalaVersion := "2.12.18"

resolvers ++= Resolver.sonatypeOssRepos("releases")

libraryDependencies ++= Seq(
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.12.6.1",
  "com.typesafe.play" %% "play-json" % "2.7.0",
  "org.slf4j" % "slf4j-api" % "1.7.25",
  "org.specs2" %% "specs2-core" % "3.8.6" % "test"
)

Test / unmanagedResourceDirectories += baseDirectory.value / "json"

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

//TODO scala version, cross scala version
/*

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}
*/

//PgpPublis
organization := "com.gu"
licenses := Seq("Apache v2" -> url("http://www.apache.org/licenses/LICENSE-2.0.html"))
scmInfo := Some(ScmInfo(
  url("https://github.com/guardian/cross-platform-navigation"),
  "scm:git:git@github.com:guardian/cross-platform-navigation.git"
))
scalacOptions ++= Seq("-deprecation", "-unchecked", "-release:11")
releaseProcess := Seq(
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  ReleaseStep(action = Command.process("publishSigned", _)),
  setNextVersion,
  commitNextVersion,
  releaseStepCommand("sonatypeReleaseAll"))

Test/testOptions += Tests.Argument(
  TestFrameworks.ScalaTest,
  "-u", s"test-results/scala-${scalaVersion.value}", "-o"
)
