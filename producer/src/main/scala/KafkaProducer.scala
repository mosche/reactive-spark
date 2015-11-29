
import java.nio.ByteBuffer
import java.security.SecureRandom

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.FlowGraph.Implicits._
import akka.stream.scaladsl._
import com.softwaremill.react.kafka.{ProducerProperties, ReactiveKafka}
import kafka.serializer.Encoder
import kafka.utils.Logging
import java.lang.{Long => JLong}
import scala.concurrent.duration._

object KafkaProducer extends App with Logging {

  val kafkaBrokers = "192.168.99.100:9092"

  val Rate = 1 seconds

  implicit val actorSystem = ActorSystem("ReactiveKafka")
  implicit val materializer = ActorMaterializer()

  val kafka = new ReactiveKafka()

  val numberSource = {
    val random = new SecureRandom()
    Source(Iterator.continually[Long](random.nextLong()).toStream)
  }

  def tick[T]: Flow[T, T, Unit] = Flow(Zip[T, Unit]()){ implicit b => zip =>
    Source(Rate, Rate, ()) ~> zip.in1
    (zip.in0, zip.out)
  }.map(_._1)


  val kafkaSink = Sink(kafka.publish(ProducerProperties(
    brokerList = kafkaBrokers,
    topic = "numbers",
    encoder = LongEncoder
  )))

  numberSource
    .via(tick)
    .collect{case l => logger.info(s"produced $l"); l}
    .to(kafkaSink)
    .run()
}

object LongEncoder extends Encoder[Long] {
  override def toBytes(long: Long): Array[Byte] = ByteBuffer.allocate(JLong.BYTES).array()
}


