package com.pygmalios.reactiveinflux.spark

import com.pygmalios.reactiveinflux.ReactiveInfluxDbParams

/**
  * Mix-in trait which provides implicit parameters for database connection.
  */
trait Influx {
  implicit def params: ReactiveInfluxDbParams
}
