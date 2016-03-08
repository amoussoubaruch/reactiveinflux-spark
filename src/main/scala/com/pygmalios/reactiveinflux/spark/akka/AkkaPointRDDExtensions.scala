package com.pygmalios.reactiveinflux.spark.akka

import akka.actor.ActorSystem
import com.pygmalios.reactiveinflux.ReactiveInfluxDbParams
import com.pygmalios.reactiveinflux.command.write.PointNoTime
import com.pygmalios.reactiveinflux.spark.config.ReactiveInfluxSparkConfig
import org.apache.spark.rdd.RDD

import scala.concurrent.duration.Duration

private[spark] class AkkaPointRDDExtensions[T <: PointNoTime](rdd: RDD[T]) extends AkkaRDDExtensions {
  override def saveToInflux()(implicit reactiveInfluxDbParams: ReactiveInfluxDbParams,
                              actorSystem: ActorSystem,
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
