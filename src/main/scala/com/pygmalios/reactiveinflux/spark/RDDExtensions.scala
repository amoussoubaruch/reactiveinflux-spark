package com.pygmalios.reactiveinflux.spark

import com.pygmalios.reactiveinflux.ReactiveInfluxDbParams
import com.pygmalios.reactiveinflux.command.write.PointNoTime
import com.pygmalios.reactiveinflux.spark.extensions.PointRDDExtensions
import org.apache.spark.rdd.RDD

import scala.concurrent.duration.Duration

trait RDDExtensions[+T <: PointNoTime] {
  def saveToInflux()(implicit reactiveInfluxDbParams: ReactiveInfluxDbParams,
                     awaitAtMost: Duration)
}

object RDDExtensions {
  def apply[T <: PointNoTime](rdd: RDD[T]): RDDExtensions[T] =
    new PointRDDExtensions(rdd)
}