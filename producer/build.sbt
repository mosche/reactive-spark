name := "reactive-spark-producer"

dockerRepository := Some("gonitro")

mainClass := Some("KafkaProducer")

libraryDependencies ++= Seq(
  "com.softwaremill.reactivekafka" %% "reactive-kafka-core" % "0.8.3",
  "ch.qos.logback" % "logback-classic" % "1.1.3"
)
    