package com.pygmalios.reactiveinflux.spark.extensions

import com.pygmalios.reactiveinflux.ReactiveInfluxDbParams
import com.pygmalios.reactiveinflux.command.write.PointNoTime
import com.pygmalios.reactiveinflux.spark.config.ReactiveInfluxSparkConfig
import com.pygmalios.reactiveinflux.spark.{RDDExtensions, _}
import org.apache.spark.rdd.RDD

import scala.concurrent.duration.Duration

private[spark] class PointRDDExtensions[+T <: PointNoTime](rdd: RDD[T]) extends RDDExtensions[T] {
  override def saveToInflux()(implicit reactiveInfluxDbParams: ReactiveInfluxDbParams,
                              awaitAtMost: Duration): Unit = {
    // Process each partition separately
    rdd.foreachPartition { partition =>
      withInflux { db =>
        val batchSize = ReactiveInfluxSparkConfig(db.config).sparkBatchSize

        // Write points in batches
        partition.sliding(batchSize).foreach { batch =>
          // Write single batch
          db.write(batch)
        }
      }
    }
  }
}
