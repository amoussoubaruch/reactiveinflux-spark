package com.pygmalios.reactiveinflux

import com.pygmalios.reactiveinflux.command.write.PointNoTime
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.DStream

package object spark {
  implicit def toRDDFunctions[T <: PointNoTime](rdd: RDD[T]): RDDExtensions = RDDExtensions(rdd)
  implicit def toDStreamFunctions[T](rdd: DStream[T]): DStreamExtensions[T] = DStreamExtensions[T](rdd)
}
