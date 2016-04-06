package com.pygmalios.reactiveinflux.extensions

import com.holdenkarau.spark.testing.SharedSparkContext
import com.pygmalios.reactiveinflux.Point.Measurement
import com.pygmalios.reactiveinflux._
import com.pygmalios.reactiveinflux.extensions.PointRDDExtensionsSpec._
import com.pygmalios.reactiveinflux.spark._
import com.pygmalios.reactiveinflux.spark.extensions.PointRDDExtensions
import org.joda.time.{DateTime, DateTimeZone}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterAll, FlatSpec}

import scala.concurrent.duration._

@RunWith(classOf[JUnitRunner])
class PointRDDExtensionsSpec extends FlatSpec with SharedSparkContext
  with BeforeAndAfterAll {

  override def beforeAll: Unit = {
    super.beforeAll
    withInflux(_.create())
  }

  override def afterAll: Unit = {
    withInflux(_.drop())
    super.afterAll
  }

  behavior of "saveToInflux"

  it should "write single point to Influx" in {
    val points = List(point1)
    val rdd = sc.parallelize(points)

    // Execute
    rdd.saveToInflux()

    // Assert
    assert(PointRDDExtensions.totalBatchCount == 1)
    assert(PointRDDExtensions.totalPointCount == 1)
    val result = withInflux(
      _.query(Query(s"SELECT * FROM $measurement1"))
      .result
      .singleSeries)

    assert(result.rows.size == 1)

    val row = result.rows.head
    assert(row.time == point1.time)
    assert(row.values.size == 5)
  }

  it should "write 1000 points to Influx" in {
    val points = (1 to 1000).map { i =>
      Point(
        time = point1.time.plusMinutes(i),
        measurement = point1.measurement,
        tags = point1.tags,
        fields = point1.fields
      )
    }
    val rdd = sc.parallelize(points)

    // Execute
    rdd.saveToInflux()

    // Assert
    assert(PointRDDExtensions.totalBatchCount == 8)
    assert(PointRDDExtensions.totalPointCount == 1000)
    val result = withInflux(
      _.query(Query(s"SELECT * FROM $measurement1"))
        .result
        .singleSeries)

    assert(result.rows.size == 1000)
  }
}

object PointRDDExtensionsSpec {
  implicit val params: ReactiveInfluxDbName = ReactiveInfluxDbName("test")
  implicit val awaitAtMost: Duration = 1.second

  val measurement1: Measurement = "measurement1"
  val point1 = Point(
    time        = new DateTime(1983, 1, 10, 7, 43, 10, 3, DateTimeZone.UTC),
    measurement = measurement1,
    tags        = Map("tagKey1" -> "tagValue1", "tagKey2" -> "tagValue2"),
    fields      = Map("fieldKey1" -> StringFieldValue("fieldValue1"), "fieldKey2" -> BigDecimalFieldValue(10.7)))
}