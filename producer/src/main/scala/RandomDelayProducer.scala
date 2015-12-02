
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.FlowGraph.Implicits._
import akka.stream.scaladsl._
import com.softwaremill.react.kafka.{ProducerProperties, ReactiveKafka}
import kafka.serializer.StringEncoder
import kafka.utils.Logging

import scala.concurrent.duration._
import scala.util.{Random, Try}

object RandomDelayProducer extends App with Logging with Environment {

  val Rate = SystemEnv.getFiniteDuration("PRODUCER_RATE")

  val MaxDelay = SystemEnv.getFiniteDuration("PRODUCER_MAX_DELAY").toMillis.toInt
  val MinDelay = SystemEnv.getFiniteDuration("PRODUCER_MIN_DELAY").toMillis.toInt
  require(MinDelay <= MaxDelay)


  implicit val actorSystem = ActorSystem("Kafka")
  implicit val materializer = ActorMaterializer()

  val kafka = new ReactiveKafka()

  val source= Source[Int] {
    def nextDelay(): Int = MinDelay + Random.nextInt(MaxDelay - MinDelay)
    Iterator.continually(nextDelay()).toStream
  }

  def tick[T]: Flow[T, T, Unit] = Flow(Zip[T, Unit]()){ implicit b => zip =>
    Source(Rate, Rate, ()) ~> zip.in1
    (zip.in0, zip.out)
  }.map(_._1)


  val kafkaSink = Sink(kafka.publish(ProducerProperties(
    brokerList = "kafka:9092",
    topic = "random",
    encoder = new StringEncoder
  ).asynchronous()))

  source
    .via(tick)
    .map(_.toString)
    .to(kafkaSink)
    .run()
}

trait Environment {

  object SystemEnv {
    def getFiniteDuration(name: String): FiniteDuration = {
      val value = System.getenv(name)
      Try(Duration(value))
        .toOption.filter(_.isFinite())
        .getOrElse(throw new Exception(s"Required finite rate, but got '$value'"))
        .asInstanceOf[FiniteDuration]
    }
  }

}


