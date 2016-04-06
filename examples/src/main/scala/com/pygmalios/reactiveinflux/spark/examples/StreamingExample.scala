package com.pygmalios.reactiveinflux.spark.examples

import com.pygmalios.reactiveinflux._
import com.pygmalios.reactiveinflux.spark._
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.joda.time.DateTime

import scala.concurrent.duration._

/**
  * This example requires that:
  * 1. There exists "application.conf" resource containing Influx URL and other settings.
  * 2. "Example" database exists in Influx.
  */
object StreamingExample extends App {
  val conf = new SparkConf()
    .setMaster("local[*]")
    .setAppName("Example")
  val ssc = new StreamingContext(conf, Seconds(1))

  val point1 = Point(
    time        = DateTime.now(),
    measurement = "measurement1",
    tags        = Map(
      "tagKey1" -> "tagValue1",
      "tagKey2" -> "tagValue2"),
    fields      = Map(
      "fieldKey1" -> "fieldValue1",
      "fieldKey2" -> 10.7)
  )

  // Provide settings for reactiveinflux
  implicit val params = ReactiveInfluxDbName("example")
  implicit val awaitAtMost = 1.second

  // Create DStream of Influx points
  val queue = new scala.collection.mutable.Queue[RDD[Point]]
  val queueStream: DStream[Point] = ssc.queueStream(queue)

  // Add single RDD with a single Influx point to the DStream
  queue.enqueue(ssc.sparkContext.parallelize(Seq(point1)))

  // Save DStream to Influx
  queueStream.saveToInflux()

  // Start Spark streaming
  ssc.start()
  ssc.awaitTermination()
}
