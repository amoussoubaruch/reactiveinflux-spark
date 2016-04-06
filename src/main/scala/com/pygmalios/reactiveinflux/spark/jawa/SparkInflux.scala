package com.pygmalios.reactiveinflux.spark.jawa

import com.pygmalios.reactiveinflux.ReactiveInfluxDbName
import com.pygmalios.reactiveinflux.jawa.{Conversions, PointNoTime}
import com.pygmalios.reactiveinflux.spark._
import com.pygmalios.{reactiveinflux => sc}
import org.apache.spark.api.java.JavaRDD
import scala.concurrent.duration._

class SparkInflux(val dbName: String,
                  val awaitAtMostMillis: Long) {
  private implicit val reactiveInfluxDbName = ReactiveInfluxDbName(dbName)
  private implicit val awaitAtMost = awaitAtMostMillis.millis

  def saveToInflux[T <: PointNoTime](javaRdd: JavaRDD[T]): Unit = {
    javaRdd.rdd.map(Conversions.toScala).saveToInflux()
  }
}
