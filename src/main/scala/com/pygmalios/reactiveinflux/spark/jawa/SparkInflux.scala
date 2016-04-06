package com.pygmalios.reactiveinflux.spark.jawa

import com.pygmalios.reactiveinflux.ReactiveInfluxDbName
import com.pygmalios.reactiveinflux.jawa.{Conversions, PointNoTime}
import com.pygmalios.reactiveinflux.spark._
import org.apache.spark.api.java.JavaRDD
import org.apache.spark.streaming.api.java.JavaDStream

import scala.concurrent.duration._

class SparkInflux(val dbName: String,
                  val awaitAtMostMillis: Long) {
  private implicit val reactiveInfluxDbName = ReactiveInfluxDbName(dbName)
  private implicit val awaitAtMost = awaitAtMostMillis.millis

  def saveToInflux[T <: PointNoTime](javaRdd: JavaRDD[T]): Unit = {
    javaRdd.rdd.map(Conversions.toScala).saveToInflux()
  }

  def saveToInflux[T <: PointNoTime](javaDStream: JavaDStream[T]): Unit = {
    javaDStream.dstream.map(Conversions.toScala).saveToInflux()
  }
}
