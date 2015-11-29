
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._
import com.softwaremill.react.kafka.KafkaMessages.KafkaMessage
import com.softwaremill.react.kafka.{ConsumerProperties, ReactiveKafka}
import kafka.utils.Logging

class KafkaConsumer extends App with Logging {

  val kafkaBrokers = "192.168.99.100:9092"

  implicit val actorSystem = ActorSystem("ReactiveKafka")
  implicit val materializer = ActorMaterializer()

  val kafka = new ReactiveKafka()

  val kafkaSource = Source(kafka.consume(ConsumerProperties(
    brokerList = kafkaBrokers,
    zooKeeperHost = "zk:2181",
    topic = "numbers",
    groupId = "default",
    decoder = new LongDecoder()
  )))

  val loggingSink = Sink.foreach[KafkaMessage[Long]] { l =>
    logger.info(s"received ${l.message}")
  }

  kafkaSource.to(loggingSink).run()
}

