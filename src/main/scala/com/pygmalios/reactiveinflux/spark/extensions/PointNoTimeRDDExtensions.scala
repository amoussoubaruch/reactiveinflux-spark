package com.pygmalios.reactiveinflux.spark.extensions

import com.pygmalios.reactiveinflux.ReactiveInflux
import com.pygmalios.reactiveinflux.command.write.PointNoTime
import com.pygmalios.reactiveinflux.spark.RDDExtensions
import org.apache.spark.rdd.RDD

private[spark] class PointNoTimeRDDExtensions[T <: PointNoTime](rdd: RDD[T]) extends RDDExtensions {
  override def saveToInflux()(implicit reactiveInflux: ReactiveInflux): Unit = {
  }
}
