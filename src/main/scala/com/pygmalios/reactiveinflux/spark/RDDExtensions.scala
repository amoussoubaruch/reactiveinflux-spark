package com.pygmalios.reactiveinflux.spark

import com.pygmalios.reactiveinflux.{ReactiveInfluxDbParams, ReactiveInflux}
import com.pygmalios.reactiveinflux.command.write.PointNoTime
import com.pygmalios.reactiveinflux.spark.extensions.PointNoTimeRDDExtensions
import org.apache.spark.rdd.RDD

import scala.concurrent.duration.Duration

trait RDDExtensions {
  def saveToInflux()(implicit reactiveInfluxDbParams: ReactiveInfluxDbParams, awaitAtMost: Duration)
}

object RDDExtensions {
  def apply[T <: PointNoTime](rdd: RDD[T]): RDDExtensions =
    new PointNoTimeRDDExtensions(rdd)
}