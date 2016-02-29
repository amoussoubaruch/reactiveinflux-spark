package com.pygmalios.reactiveinflux

import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.DStream

package object spark {
  implicit def toRDDFunctions[T](rdd: RDD[T]): RDDExtensions[T] = RDDExtensions[T](rdd)
  implicit def toDStreamFunctions[T](rdd: DStream[T]): DStreamExtensions[T] = DStreamExtensions[T](rdd)
}
