package com.pygmalios.reactiveinflux.spark

import org.apache.spark.streaming.dstream.DStream

trait DStreamExtensions[+T] {
}

object DStreamExtensions {
  def apply[T](dstream: DStream[T]): DStreamExtensions[T] = {
    ???
  }
}