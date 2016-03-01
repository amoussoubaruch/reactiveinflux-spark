package com.pygmalios.reactiveinflux.spark.extensions

import com.pygmalios.reactiveinflux.ReactiveInfluxDbParams
import com.pygmalios.reactiveinflux.command.write.PointNoTime
import com.pygmalios.reactiveinflux.spark.RDDExtensions
import com.pygmalios.reactiveinflux.spark.config.ReactiveInfluxSparkConfig
import com.pygmalios.reactiveinflux.sync.SyncReactiveInflux
import org.apache.spark.rdd.RDD

import scala.concurrent.duration.Duration

private[spark] class PointNoTimeRDDExtensions[T <: PointNoTime](rdd: RDD[T]) extends RDDExtensions {
  override def saveToInflux()(implicit reactiveInfluxDbParams: ReactiveInfluxDbParams, awaitAtMost: Duration): Unit = {
    // Process each partition separately
    rdd.foreachPartition { partition =>
      val syncReactiveInflux = SyncReactiveInflux()
      try {
        val batchSize = ReactiveInfluxSparkConfig(syncReactiveInflux.config).sparkBatchSize

        // Write points in batches
        partition.sliding(batchSize).foreach { batch =>
          // Write single batch
          syncReactiveInflux.database.write(batch)
        }
      }
      finally {
        syncReactiveInflux.close()
      }
    }
  }
}
