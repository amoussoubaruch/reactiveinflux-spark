package com.pygmalios.reactiveinflux.spark.config

import com.pygmalios.reactiveinflux.ReactiveInfluxConfig

private[spark] class ReactiveInfluxSparkConfig(reactiveInfluxConfig: ReactiveInfluxConfig)  {
  val spark = reactiveInfluxConfig.reactiveinflux.getConfig("spark")
  val sparkBatchSize = spark.getInt("batchSize")
}

private[spark] object ReactiveInfluxSparkConfig {
  def apply(reactiveInfluxConfig: ReactiveInfluxConfig) =
    new ReactiveInfluxSparkConfig(reactiveInfluxConfig)
}