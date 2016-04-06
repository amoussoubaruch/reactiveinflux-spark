package com.pygmalios.reactiveinflux.extensions

import com.holdenkarau.spark.testing.StreamingActionBase
import com.pygmalios.reactiveinflux.spark._
import com.pygmalios.reactiveinflux._
import org.apache.spark.streaming.dstream.DStream
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PointDStreamExtensionsSpec extends StreamingActionBase
  with BeforeAndAfterAll {
  import PointRDDExtensionsSpec._

  override def beforeAll: Unit = {
    super.beforeAll
    withInflux(_.create())
  }

  override def afterAll: Unit = {
    withInflux(_.drop())
    super.afterAll
  }

  test("write single point to Influx") {
    val points = List(point1)

    // Execute
    runAction(Seq(points), (dstream: DStream[Point]) => dstream.saveToInflux())

    // Assert
    val result = withInflux(
      _.query(Query(s"SELECT * FROM $measurement1")).result.singleSeries)

    assert(result.rows.size == 1)

    val row = result.rows.head
    assert(row.time == point1.time)
    assert(row.values.size == 5)
  }
}