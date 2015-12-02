import sbt._

name := "reactive-spark"

version in ThisBuild := "latest"

sourcesInBase := false

lazy val root = project.in(file("."))
  .aggregate(consumer, producer)

lazy val consumer = project
  .enablePlugins(JavaAppPackaging)

lazy val producer = project
  .enablePlugins(JavaAppPackaging)

