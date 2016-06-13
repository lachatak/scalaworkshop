import sbt._
import sbt.Keys._

object Version {

  val akka          = "2.4.4"
  val scalaLogging  = "3.4.0"
  val jodaTime      = "2.8.2"
  val scalazCore    = "7.2.2"
  val scalaTest     = "2.2.6"
  val config        = "1.2.1"
  val logBack       = "1.1.7"
  val mockito       = "1.10.19"
}

object Library {
  val akkaActor       = "com.typesafe.akka"           %% "akka-actor"                    % Version.akka
  val akkaTestkit     = "com.typesafe.akka"           %% "akka-testkit"                  % Version.akka
  val akkaSlf4j       = "com.typesafe.akka"           %% "akka-slf4j"                    % Version.akka
  val scalaLogging    = "com.typesafe.scala-logging"  %% "scala-logging"                 % Version.scalaLogging
  val logBack         = "ch.qos.logback"              %  "logback-classic"               % Version.logBack
  val jodaTime        = "joda-time"                   %  "joda-time"                     % Version.jodaTime
  val config          = "com.typesafe" 	              %  "config" 		                   % Version.config
  val scalazCore      = "org.scalaz"                  %% "scalaz-core"                   % Version.scalazCore
  val mockito         = "org.mockito"                 %  "mockito-core"                  % Version.mockito
  val scalaTest       = "org.scalatest"               %% "scalatest"                     % Version.scalaTest
}

object Dependencies {

  import Library._

  val basics = deps(
    mockito       	% "test",
    scalaTest     	% "test"
  )

  private def deps(modules: ModuleID*): Seq[Setting[_]] = Seq(libraryDependencies ++= modules)
}
