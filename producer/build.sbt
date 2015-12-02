name := "reactive-spark-producer"

scalaVersion := "2.11.7"

dockerRepository := Some("gonitro")

mainClass := Some("RandomDelayProducer")

libraryDependencies ++= Seq(
  "com.softwaremill.reactivekafka" %% "reactive-kafka-core" % "0.8.3",
  "ch.qos.logback" % "logback-classic" % "1.1.3"
)
    