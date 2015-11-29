import java.lang.{Long => JLong}
import java.nio.ByteBuffer

import _root_.kafka.serializer.{Decoder, StringDecoder}
import _root_.kafka.utils.VerifiableProperties
import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._

class SparkConsumer {

  val sparkConf = new SparkConf().setAppName("KafkaWordCount")
  val ssc = new StreamingContext(sparkConf, Seconds(2))
  ssc.checkpoint("checkpoint")

  val params = Map(
    "zookeeper.connect" -> "zk",
    "group.id" -> "sparkconsumer",
    "zookeeper.connection.timeout.ms" -> "10000")

  val topics = Map(
    "numbers" -> 1
  )

  val numbers = KafkaUtils.createStream[String, Long, StringDecoder, LongDecoder](ssc, params, topics, StorageLevel.MEMORY_AND_DISK_SER_2)

  numbers.map(_._2)
    .reduceByWindow(_ + _, Seconds(10), Seconds(2))
    .print()


  ssc.start()
  ssc.awaitTermination()
}

class LongDecoder(props: VerifiableProperties = null) extends Decoder[Long] {
  override def fromBytes(bytes: Array[Byte]): Long = ByteBuffer.allocate(JLong.BYTES).put(bytes).getLong
}