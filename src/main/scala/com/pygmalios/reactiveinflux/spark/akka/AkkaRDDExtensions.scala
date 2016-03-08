package com.pygmalios.reactiveinflux.spark.akka

import akka.actor.ActorSystem
import com.pygmalios.reactiveinflux.ReactiveInfluxDbParams
import com.pygmalios.reactiveinflux.command.write.PointNoTime
import org.apache.spark.rdd.RDD

import scala.concurrent.duration.Duration

trait AkkaRDDExtensions {
  def saveToInflux()(implicit reactiveInfluxDbParams: ReactiveInfluxDbParams,
                     actorSystem: ActorSystem,
                     awaitAtMost: Duration)
}

object AkkaRDDExtensions {
  def apply[T <: PointNoTime](rdd: RDD[T]): AkkaRDDExtensions =
    new AkkaPointRDDExtensions(rdd)
}