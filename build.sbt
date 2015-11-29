name := "reactive-spark"

version in ThisBuild := "latest"

sourcesInBase := false

scalaVersion in ThisBuild := "2.11.7"

lazy val root = project.in(file("."))
  .aggregate(consumer, producer)

lazy val consumer = project
  .enablePlugins(JavaAppPackaging)

lazy val producer = project
  .enablePlugins(JavaAppPackaging)

