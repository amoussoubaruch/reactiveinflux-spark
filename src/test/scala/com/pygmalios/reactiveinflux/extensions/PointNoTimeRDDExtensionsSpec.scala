package com.pygmalios.reactiveinflux.extensions

import com.holdenkarau.spark.testing.SharedSparkContext
import com.pygmalios.reactiveinflux._
import com.pygmalios.reactiveinflux.command.write.Point.Measurement
import com.pygmalios.reactiveinflux.command.write.{BigDecimalFieldValue, Point, StringFieldValue}
import com.pygmalios.reactiveinflux.spark._
import org.joda.time.{DateTime, DateTimeZone}
import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfterAll, FlatSpec}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PointNoTimeRDDExtensionsSpec extends FlatSpec with SharedSparkContext with BeforeAndAfterAll {
  import PointNoTimeRDDExtensionsSpec._

  implicit val reactiveInflux = ReactiveInflux()

  override def afterAll: Unit = {
    super.afterAll()
    reactiveInflux.close()
  }

  behavior of "saveToInflux"

  it should "write single point to Influx" in {
    val points = List(point1)
    val rdd = sc.parallelize(points)

    rdd.saveToInflux()
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