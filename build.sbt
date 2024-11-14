ThisBuild / scalaVersion := "3.5.2"
ThisBuild / organization := "com.server"

lazy val server = project 
    .in(file("."))
    .settings (
        name := "server",
        libraryDependencies ++= Seq(
            "org.scala-lang" %% "toolkit" % "0.1.7",
            "com.lihaoyi" %% "requests" % "0.8.0",
        )
    )