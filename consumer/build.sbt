name := "reactive-spark-consumer"

dockerRepository := Some("gonitro")

mainClass := Some("SparkConsumer")

libraryDependencies ++= Seq(
  "com.softwaremill.reactivekafka" %% "reactive-kafka-core" % "0.8.3",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "org.apache.spark" %% "spark-streaming-kafka" % "1.5.2",
  "org.apache.spark" %% "spark-streaming" % "1.5.2" % Provided,
  "org.apache.spark" %% "spark-core" % "1.5.2" % Provided
)

assemblyMergeStrategy in assembly := {
  case p if p.endsWith(".MF") =>  MergeStrategy.discard
  case _ => MergeStrategy.first
}
    