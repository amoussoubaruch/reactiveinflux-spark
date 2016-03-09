package com.pygmalios.reactiveinflux.spark

import com.pygmalios.reactiveinflux.ReactiveInfluxDbParams
import com.pygmalios.reactiveinflux.command.write.PointNoTime
import com.pygmalios.reactiveinflux.spark.extensions.PointDStreamExtensions
import org.apache.spark.streaming.dstream.DStream

import scala.concurrent.duration.Duration

trait DStreamExtensions[+T <: PointNoTime] {
  def saveToInflux()(implicit reactiveInfluxDbParams: ReactiveInfluxDbParams,
                     awaitAtMost: Duration)
}

object DStreamExtensions {
  def apply[T <: PointNoTime](dstream: DStream[T]): DStreamExtensions[T] =
    new PointDStreamExtensions(dstream)
}