enablePlugins(SparkSubmitPlugin)

name := "reactive-spark-consumer"

scalaVersion := "2.10.4"

dockerRepository := Some("gonitro")

mainClass := Some("SparkConsumer")

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-streaming-kafka" % "1.5.2",
  "org.apache.spark" %% "spark-streaming" % "1.5.2" % Provided,
  "org.apache.spark" %% "spark-core" % "1.5.2" % Provided
)

assemblyMergeStrategy in assembly := {
  case p if p.endsWith(".MF") =>  MergeStrategy.discard
  case _ => MergeStrategy.first
}

sparkSubmitJar := (assemblyOutputPath in assembly).value.absolutePath
sparkSubmitMaster := {(_,_) => s"spark://master:7077"}
sparkSubmitSparkArgs += "-v"

