package com.pygmalios.reactiveinflux.spark

import com.pygmalios.reactiveinflux.ReactiveInflux
import com.pygmalios.reactiveinflux.command.write.PointNoTime
import com.pygmalios.reactiveinflux.spark.extensions.PointNoTimeRDDExtensions
import org.apache.spark.rdd.RDD

trait RDDExtensions {
  def saveToInflux()(implicit reactiveInflux: ReactiveInflux)
}

object RDDExtensions {
  def apply[T <: PointNoTime](rdd: RDD[T]): RDDExtensions =
    new PointNoTimeRDDExtensions(rdd)
}