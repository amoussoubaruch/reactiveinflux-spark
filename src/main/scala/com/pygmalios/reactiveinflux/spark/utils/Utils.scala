package com.pygmalios.reactiveinflux.spark.utils

import com.pygmalios.reactiveinflux.ReactiveInfluxDbParams
import com.pygmalios.reactiveinflux.sync.{SyncReactiveInflux, SyncReactiveInfluxDb}

import scala.concurrent.duration.Duration

object Utils {
  def withInflux[S](action: (SyncReactiveInfluxDb) => S)
                   (implicit reactiveInfluxDbParams: ReactiveInfluxDbParams,
                    awaitAtMost: Duration): S = {
    val syncReactiveInflux = SyncReactiveInflux()
    try {
      action(syncReactiveInflux.database)
    }
    finally {
      syncReactiveInflux.close()
    }
  }
}
