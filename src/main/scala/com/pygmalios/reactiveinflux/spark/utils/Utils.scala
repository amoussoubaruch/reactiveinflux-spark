package com.pygmalios.reactiveinflux.spark.utils

import com.pygmalios.reactiveinflux.sync.{SyncReactiveInflux, SyncReactiveInfluxDb}
import com.pygmalios.reactiveinflux.{ReactiveInflux, ReactiveInfluxDbParams}

import scala.concurrent.duration.Duration

object Utils {
  def withInflux[S](action: (SyncReactiveInfluxDb) => S)
                   (implicit reactiveInfluxDbParams: ReactiveInfluxDbParams,
                    awaitAtMost: Duration): S = {
    val syncReactiveInflux = SyncReactiveInflux(
      name = ReactiveInflux.defaultClientName,
      config = None,
      clientFactory = ReactiveInflux.defaultClientFactory)
    try {
      action(syncReactiveInflux.database)
    }
    finally {
      syncReactiveInflux.close()
    }
  }
}
