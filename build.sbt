import sbtrelease.ReleaseStateTransformations._
import sbtversionpolicy.withsbtrelease.ReleaseVersion

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

description := "Provides scala representation of the navigation menus for the www.theguardian.com and guardian apps"

organization := "com.gu"
licenses := Seq(License.Apache2)
scalacOptions ++= Seq("-deprecation", "-unchecked", "-release:11")
releaseCrossBuild := true
releaseVersion := ReleaseVersion.fromAggregatedAssessedCompatibilityWithLatestRelease().value
releaseProcess := Seq(
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  setNextVersion,
  commitNextVersion
)

Test/testOptions += Tests.Argument(
  TestFrameworks.ScalaTest,
  "-u", s"test-results/scala-${scalaVersion.value}", "-o"
)
