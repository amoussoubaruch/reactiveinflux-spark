package com.pygmalios.reactiveinflux.spark.utils

import com.pygmalios.reactiveinflux.{ReactiveInfluxConfig, ReactiveInfluxDbName}
import com.pygmalios.reactiveinflux.sync.{SyncReactiveInflux, SyncReactiveInfluxDb}

import scala.concurrent.duration.Duration

object Utils {
  def withInflux[S](action: (SyncReactiveInfluxDb) => S)
                   (implicit reactiveInfluxDbName: ReactiveInfluxDbName,
                    awaitAtMost: Duration): S = {
    val syncReactiveInflux = SyncReactiveInflux(ReactiveInfluxConfig(None))
    try {
      action(syncReactiveInflux.database)
    }
    finally {
      syncReactiveInflux.close()
    }
  }
}
