package com.pygmalios.reactiveinflux.spark

import com.pygmalios.reactiveinflux.spark.extensions.PointRDDExtensions
import com.pygmalios.reactiveinflux.{PointNoTime, ReactiveInfluxDbName}
import org.apache.spark.rdd.RDD

import scala.concurrent.duration.Duration

trait RDDExtensions[+T <: PointNoTime] {
  def saveToInflux()(implicit reactiveInfluxDbParams: ReactiveInfluxDbName,
                     awaitAtMost: Duration)
}

object RDDExtensions {
  def apply[T <: PointNoTime](rdd: RDD[T]): RDDExtensions[T] =
    new PointRDDExtensions(rdd)
}