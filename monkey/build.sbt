import Dependencies._

val scalazVersion = "7.2.9"
val scalaMayorVersion = "2.12"
val scalaMinorVersion = s"$scalaMayorVersion.1"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.fhuertas",
      scalaVersion := s"$scalaMinorVersion",
      version := "0.1.0-SNAPSHOT"
    )),
    name := "Monkey",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      "org.scalaz" % s"scalaz-core_$scalaMayorVersion" % s"$scalazVersion",
      "com.typesafe" % "config" % "1.3.1",
      "org.scalatest" % s"scalatest_$scalaMayorVersion" % "3.0.1"
    )
  )

