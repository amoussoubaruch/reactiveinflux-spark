package com.pygmalios.reactiveinflux.spark.akka

import akka.actor.ActorSystem
import com.pygmalios.reactiveinflux.ReactiveInfluxDbParams
import com.pygmalios.reactiveinflux.sync.{SyncReactiveInflux, SyncReactiveInfluxDb}

import scala.concurrent.duration.Duration

object AkkaUtils {
  def withInflux[S](action: (SyncReactiveInfluxDb) => S)
                   (implicit reactiveInfluxDbParams: ReactiveInfluxDbParams,
                    actorSystem: ActorSystem,
                    awaitAtMost: Duration): S = {
    val syncReactiveInflux = SyncReactiveInflux(actorSystem)
    try {
      action(syncReactiveInflux.database)
    }
    finally {
      syncReactiveInflux.close()
    }
  }
}
