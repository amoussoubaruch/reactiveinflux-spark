package com.pygmalios.reactiveinflux.spark

import com.pygmalios.reactiveinflux.ReactiveInfluxDbName
import com.pygmalios.reactiveinflux.sync.SyncReactiveInfluxDb

import scala.concurrent.duration.Duration

/**
  * Utility methods.
  */
trait InfluxUtils {
  implicit def withInflux[S](action: (SyncReactiveInfluxDb) => S)
                            (implicit reactiveInfluxDbParams: ReactiveInfluxDbName,
                             awaitAtMost: Duration): S
}
