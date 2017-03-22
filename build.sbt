import Dependencies._

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
      akkaTest % Test,
      scalaz,
      typesafe_config,
      typesafe_logging,
      haikunator,
      logback,
      akka
    )
  )
