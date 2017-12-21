import sbtrelease.ReleaseStateTransformations._

name:="cross-platform-navigation"

scalaVersion in ThisBuild := "2.11.8"

resolvers += Resolver.sonatypeRepo("releases")

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

pomExtra in Global := {
  <url>https://github.com/guardian/mobile-notifications-api-client</url>
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
