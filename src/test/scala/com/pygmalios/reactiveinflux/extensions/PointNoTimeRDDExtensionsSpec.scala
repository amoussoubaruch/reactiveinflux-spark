package com.pygmalios.reactiveinflux.extensions

import com.holdenkarau.spark.testing.SharedSparkContext
import com.pygmalios.reactiveinflux._
import com.pygmalios.reactiveinflux.command.query.Query
import com.pygmalios.reactiveinflux.command.write.Point.Measurement
import com.pygmalios.reactiveinflux.command.write.{BigDecimalFieldValue, Point, StringFieldValue}
import com.pygmalios.reactiveinflux.spark._
import com.pygmalios.reactiveinflux.sync.SyncReactiveInflux
import org.joda.time.{DateTime, DateTimeZone}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterAll, FlatSpec}
import scala.concurrent.duration._

@RunWith(classOf[JUnitRunner])
class PointNoTimeRDDExtensionsSpec extends FlatSpec with SharedSparkContext
  with BeforeAndAfterAll with Influx {
  import PointNoTimeRDDExtensionsSpec._

  private val syncReactiveInflux = SyncReactiveInflux()
  override implicit val params = ReactiveInfluxDbParams(dbName = "test")
  implicit val awaitAtMost = 1.second

  override def beforeAll: Unit = {
    super.beforeAll
    syncReactiveInflux.database.create()
  }

  override def afterAll: Unit = {
    syncReactiveInflux.database.drop()
    super.afterAll
  }

  behavior of "saveToInflux"

  it should "write single point to Influx" in {
    val points = List(point1)
    val rdd = sc.parallelize(points)

    // Execute
    rdd.saveToInflux()

    // Assert
    val result = syncReactiveInflux
      .database
      .query(Query(s"SELECT * FROM $measurement1"))
      .result
      .single

    assert(result.values.size == 1)

    val row = result.values.head
    assert(row.time == point1.time)
    assert(row.items.size == 5)
  }
}

object PointNoTimeRDDExtensionsSpec {
  val measurement1: Measurement = "measurement1"
  val point1 = Point(
    time        = new DateTime(1983, 1, 10, 7, 43, 10, 3, DateTimeZone.UTC),
    measurement = measurement1,
    tags        = Map("tagKey1" -> "tagValue1", "tagKey2" -> "tagValue2"),
    fields      = Map("fieldKey1" -> StringFieldValue("fieldValue1"), "fieldKey2" -> BigDecimalFieldValue(10.7)))
}