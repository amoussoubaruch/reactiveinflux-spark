package com.pygmalios.reactiveinflux

import com.pygmalios.reactiveinflux.spark.utils.Utils
import com.pygmalios.reactiveinflux.sync.SyncReactiveInfluxDb
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.DStream

import scala.concurrent.duration.Duration

package object spark {
  implicit def toRDDFunctions[T <: PointNoTime](rdd: RDD[T]): RDDExtensions[T] =
    RDDExtensions(rdd)
  implicit def toDStreamFunctions[T <: PointNoTime](dStream: DStream[T]): DStreamExtensions[T] =
    DStreamExtensions(dStream)
  implicit def withInflux[S](action: (SyncReactiveInfluxDb) => S)
                            (implicit reactiveInfluxDbName: ReactiveInfluxDbName,
                             awaitAtMost: Duration): S = Utils.withInflux[S](action)
}
