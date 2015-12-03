
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.spark.{Logging, SparkConf}

object SparkConsumer extends App with Logging {

  val sparkConf = new SparkConf()
    .setAppName("SparkStreamingConsumer")
    // Reasonable upper bounds are always recommended.
    // These are particularly helpful whenever restarting your streaming job
    // after events accumulated.
    //.set("spark.streaming.kafka.maxRatePerPartition", "1000")
    //.set("spark.streaming.receiver.maxRate", "1000")
    .set("spark.streaming.backpressure.enabled","true")
    // Even using backpressure, the lower bound defaults to 100.
    // You might wanna change this for longer running jobs.
    //.set("spark.streaming.backpressure.pid.minRate", "50")

  val ssc = new StreamingContext(sparkConf, Seconds(2))
  ssc.checkpoint("checkpoint")

  val topics = Map(
    "random" -> 1
  )

  val numbers = KafkaUtils.createStream(
    ssc,
    "zk:2181",
    "spark-streaming-consumer",
    topics,
    StorageLevel.MEMORY_ONLY
  )

  numbers.map { t =>
    Thread.sleep(t._2.toLong)
    t._2.toLong
  }
    .reduceByWindow(_ + _, Seconds(10), Seconds(2))
    .print()


  ssc.start()
  ssc.awaitTermination()
}