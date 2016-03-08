package com.pygmalios.reactiveinflux.spark

import com.pygmalios.reactiveinflux.ReactiveInfluxDbParams
import com.pygmalios.reactiveinflux.sync.SyncReactiveInfluxDb

import scala.concurrent.duration.Duration

/**
  * Utility methods.
  */
trait InfluxUtils {
  implicit def withInflux[S](action: (SyncReactiveInfluxDb) => S)
                            (implicit reactiveInfluxDbParams: ReactiveInfluxDbParams,
                             awaitAtMost: Duration): S
}
