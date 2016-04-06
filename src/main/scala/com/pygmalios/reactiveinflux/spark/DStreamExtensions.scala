package com.pygmalios.reactiveinflux.spark

import com.pygmalios.reactiveinflux.spark.extensions.PointDStreamExtensions
import com.pygmalios.reactiveinflux.{PointNoTime, ReactiveInfluxDbName}
import org.apache.spark.streaming.dstream.DStream

import scala.concurrent.duration.Duration

trait DStreamExtensions[+T <: PointNoTime] {
  def saveToInflux()(implicit reactiveInfluxDbParams: ReactiveInfluxDbName,
                     awaitAtMost: Duration)
}

object DStreamExtensions {
  def apply[T <: PointNoTime](dstream: DStream[T]): DStreamExtensions[T] =
    new PointDStreamExtensions(dstream)
}