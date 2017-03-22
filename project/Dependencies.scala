import sbt._


object Dependencies {
  val scalazVersion = "7.2.9"
  val scalaMayorVersion = "2.12"
  val scalaMinorVersion = s"$scalaMayorVersion.1"
  val akkaVersion = "2.4.17"
  val logbackVersion = "1.1.3"

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1"
  lazy val scalaz = "org.scalaz" % s"scalaz-core_$scalaMayorVersion" % s"$scalazVersion"
  lazy val typesafe_config = "com.typesafe" % "config" % "1.3.1"
  lazy val typesafe_logging = "com.typesafe.scala-logging" % s"scala-logging_$scalaMayorVersion" % "3.5.0"
  lazy val haikunator = "me.atrox.haikunator" % "Haikunator" % "1.3"
  lazy val akkaTest =  "com.typesafe.akka" % s"akka-testkit_$scalaMayorVersion" % s"$akkaVersion"
  lazy val akka = "com.typesafe.akka" % s"akka-actor_$scalaMayorVersion" % s"$akkaVersion"
  lazy val logback = "ch.qos.logback" % "logback-classic" % s"$logbackVersion"
}
