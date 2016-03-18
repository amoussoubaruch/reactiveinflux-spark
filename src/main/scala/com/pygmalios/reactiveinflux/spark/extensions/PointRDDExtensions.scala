package com.pygmalios.reactiveinflux.spark.extensions

import com.pygmalios.reactiveinflux.ReactiveInfluxDbParams
import com.pygmalios.reactiveinflux.command.write.PointNoTime
import com.pygmalios.reactiveinflux.spark.config.ReactiveInfluxSparkConfig
import com.pygmalios.reactiveinflux.spark.{RDDExtensions, _}
import org.apache.spark.rdd.RDD
import org.slf4j.LoggerFactory

import scala.concurrent.duration.Duration

private[spark] class PointRDDExtensions[+T <: PointNoTime](rdd: RDD[T]) extends RDDExtensions[T] {
  import PointRDDExtensions._

  override def saveToInflux()(implicit reactiveInfluxDbParams: ReactiveInfluxDbParams,
                              awaitAtMost: Duration): Unit = {
    // Process each partition separately
    totalBatchCount = 0
    totalPointCount = 0
    rdd.foreachPartition { partition =>
      withInflux { db =>
        val batchSize = ReactiveInfluxSparkConfig(db.config).sparkBatchSize

        // Write points in batches
        var batchCount = 0
        var pointCount = 0
        partition.sliding(batchSize, batchSize).foreach { batch =>
          // Write single batch
          db.write(batch)

          // Statistics for logging
          batchCount += 1
          pointCount += batch.size
        }

        totalBatchCount += batchCount
        totalPointCount += pointCount

        log.debug(s"Partition with $pointCount points written to Influx in $batchCount batches.")
      }
    }
    log.info(s"RDD with ${rdd.partitions.size} partitions and $totalPointCount points written to Influx in $totalBatchCount batches.")
  }
}

object PointRDDExtensions {
  private val log = LoggerFactory.getLogger(classOf[PointRDDExtensions[_]])

  // This makes sense for testing purposes only
  private[reactiveinflux] var totalBatchCount = 0
  private[reactiveinflux] var totalPointCount = 0
}