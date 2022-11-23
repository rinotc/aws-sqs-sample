ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "sqs-sample",
    libraryDependencies ++= Seq(
      "ch.qos.logback"         % "logback-classic" % "1.4.5",
      "org.scalatest"         %% "scalatest"       % "3.2.14",
      "software.amazon.awssdk" % "sqs"             % "2.18.21"
    )
  )
