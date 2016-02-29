package com.pygmalios.reactiveinflux.spark

import com.pygmalios.reactiveinflux.ReactiveInflux
import org.apache.spark.rdd.RDD

trait RDDExtensions[+T] {
  def saveToInflux()(implicit reactiveInflux: ReactiveInflux)
}

object RDDExtensions {
  def apply[T](rdd: RDD[T]): RDDExtensions[T] = {
    ??? // TODO
  }
}

private class RDDExtensionsImpl[+T] extends RDDExtensions[T] {
  override def saveToInflux()(implicit reactiveInflux: ReactiveInflux): Unit = ???
}