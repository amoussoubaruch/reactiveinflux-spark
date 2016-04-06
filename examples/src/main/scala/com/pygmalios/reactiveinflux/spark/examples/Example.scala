package com.pygmalios.reactiveinflux.spark.examples

import com.pygmalios.reactiveinflux._
import com.pygmalios.reactiveinflux.spark._
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.joda.time.DateTime

import scala.concurrent.duration._

/**
  * This example requires that:
  * 1. There exists "application.conf" resource containing Influx URL and other settings.
  * 2. "Example" DB is created in Influx.
  */
object Example extends App {
  val conf = new SparkConf()
    .setMaster("local[*]")
    .setAppName("Example")
  val sc = new SparkContext(conf)

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

  // Create RDD with Influx point
  val rdd: RDD[Point] = sc.parallelize(Seq(point1))

  // Save RDD to Influx
  rdd.saveToInflux()

  // Stop Spark context
  sc.stop()
}
