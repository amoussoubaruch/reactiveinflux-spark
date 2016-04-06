package com.pygmalios.reactiveinflux.spark.extensions

import com.pygmalios.reactiveinflux.spark.{DStreamExtensions, _}
import com.pygmalios.reactiveinflux.{PointNoTime, ReactiveInfluxDbName}
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.DStream

import scala.concurrent.duration.Duration

class PointDStreamExtensions[+T <: PointNoTime](dstream: DStream[T]) extends DStreamExtensions[T] {
  override def saveToInflux()(implicit reactiveInfluxDbParams: ReactiveInfluxDbName,
                              awaitAtMost: Duration): Unit = {
    dstream.foreachRDD { rdd: RDD[T] =>
      rdd.saveToInflux()
    }
  }
}
