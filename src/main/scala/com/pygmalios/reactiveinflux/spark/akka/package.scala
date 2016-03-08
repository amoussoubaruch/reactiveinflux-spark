package com.pygmalios.reactiveinflux.spark

import _root_.akka.actor.ActorSystem
import com.pygmalios.reactiveinflux.ReactiveInfluxDbParams
import com.pygmalios.reactiveinflux.command.write.PointNoTime
import com.pygmalios.reactiveinflux.spark.utils.Utils
import com.pygmalios.reactiveinflux.sync.SyncReactiveInfluxDb
import org.apache.spark.rdd.RDD

import scala.concurrent.duration.Duration

package object akka {
  implicit def toRDDFunctions[T <: PointNoTime](rdd: RDD[T]): AkkaRDDExtensions = AkkaRDDExtensions(rdd)
  implicit def withInflux[S](action: (SyncReactiveInfluxDb) => S)
                            (implicit reactiveInfluxDbParams: ReactiveInfluxDbParams,
                             actorSystem: ActorSystem,
                             awaitAtMost: Duration): S = Utils.withInflux[S](action)

}
