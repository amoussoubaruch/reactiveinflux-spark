# reactiveinflux-spark 0.4.1-SNAPSHOT

[Apache Spark](http://spark.apache.org/) support for [reactiveinflux - non-blocking InfluxDB driver for Scala and Java](https://github.com/pygmalios/reactiveinflux).

## Get it from Maven Central repository

**Maven:**
```xml
<dependency>
  <groupId>com.pygmalios</groupId>
  <artifactId>reactiveinflux-spark_2.10</artifactId>
  <version>1.4.0.10.0.4.1-SNAPSHOT</version>
</dependency>
```

**SBT:**

```
libraryDependencies += "com.pygmalios" % "reactiveinflux" %% "0.10.0.4"
```

## Compatibility

- Apache Spark 1.4 and above
- InfluxDB 0.11, 0.10 and 0.9 (maybe even older too)
- Scala 2.11 and 2.10
- Java 7 and above

## Scala example

```scala
package com.pygmalios.reactiveinflux.spark.examples

import com.pygmalios.reactiveinflux._
import com.pygmalios.reactiveinflux.spark._
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.joda.time.DateTime

import scala.concurrent.duration._

/**
  * This example requires that:
  * 1. There exists "application.conf" resource containing Influx URL and other settings.
  * 2. "Example" database exists in Influx.
  */
object Example extends App {
  val conf = new SparkConf()
    .setMaster("local[*]")
    .setAppName("Example")
  val sc = new SparkContext(conf)

  val point1 = Point(
    time        = DateTime.now(),
    measurement = "measurement1",
    tags        = Map(
      "tagKey1" -> "tagValue1",
      "tagKey2" -> "tagValue2"),
    fields      = Map(
      "fieldKey1" -> "fieldValue1",
      "fieldKey2" -> 10.7)
  )

  // Provide settings for reactiveinflux
  implicit val params = ReactiveInfluxDbName("example")
  implicit val awaitAtMost = 1.second

  // Create RDD with Influx point
  val rdd: RDD[Point] = sc.parallelize(Seq(point1))

  // Save RDD to Influx
  rdd.saveToInflux()

  // Stop Spark context
  sc.stop()
}
```

## Scala streaming example

```scala
package com.pygmalios.reactiveinflux.spark.examples

import com.pygmalios.reactiveinflux._
import com.pygmalios.reactiveinflux.spark._
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.joda.time.DateTime

import scala.concurrent.duration._

/**
  * This example requires that:
  * 1. There exists "application.conf" resource containing Influx URL and other settings.
  * 2. "Example" database exists in Influx.
  */
object StreamingExample extends App {
  val conf = new SparkConf()
    .setMaster("local[*]")
    .setAppName("Example")
  val ssc = new StreamingContext(conf, Seconds(1))

  val point1 = Point(
    time        = DateTime.now(),
    measurement = "measurement1",
    tags        = Map(
      "tagKey1" -> "tagValue1",
      "tagKey2" -> "tagValue2"),
    fields      = Map(
      "fieldKey1" -> "fieldValue1",
      "fieldKey2" -> 10.7)
  )

  // Provide settings for reactiveinflux
  implicit val params = ReactiveInfluxDbName("example")
  implicit val awaitAtMost = 1.second

  // Create DStream of Influx points
  val queue = new scala.collection.mutable.Queue[RDD[Point]]
  val queueStream: DStream[Point] = ssc.queueStream(queue)

  // Add single RDD with a single Influx point to the DStream
  queue.enqueue(ssc.sparkContext.parallelize(Seq(point1)))

  // Save DStream to Influx
  queueStream.saveToInflux()

  // Start Spark streaming
  ssc.start()
  ssc.awaitTermination()
}
```

## Java example

```java
package com.pygmalios.reactiveinflux.spark.examples;

import com.pygmalios.reactiveinflux.jawa.*;
import com.pygmalios.reactiveinflux.spark.jawa.SparkInflux;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.joda.time.DateTime;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This example requires that:
 * 1. There exists "application.conf" resource containing Influx URL and other settings.
 * 2. "Example" database exists in Influx.
 */
public class JavaExample {
    public static void main(String[] args) {
        // Initialize Spark
        SparkConf conf = new SparkConf()
            .setMaster("local[*]")
            .setAppName("Example");
        JavaSparkContext sc = new JavaSparkContext(conf);

        // Initialize SparkInflux instance
        SparkInflux sparkInflux = new SparkInflux("example", 1000);

        // Define tags for the point
        Map<String, String> tags = new HashMap<>();
        tags.put("t1", "A");
        tags.put("t2", "B");

        // Define fields for the point
        Map<String, Object> fields = new HashMap<>();
        fields.put("f1", 10.3);
        fields.put("f2", "x");
        fields.put("f3", -1);
        fields.put("f4", true);

        // Write a single point to "measurement1"
        Point point = new JavaPoint(
                DateTime.now(),
                "measurement1",
                tags,
                fields
        );

        // Create RDD with Influx point
        JavaRDD<Point> rdd = sc.parallelize(Collections.singletonList(point));

        // Save RDD to Influx
        sparkInflux.saveToInflux(rdd);

        // Stop Spark context
        sc.stop();
    }
}
```

## Java streaming example

```java
package com.pygmalios.reactiveinflux.spark.examples;

import com.pygmalios.reactiveinflux.jawa.JavaPoint;
import com.pygmalios.reactiveinflux.jawa.Point;
import com.pygmalios.reactiveinflux.spark.jawa.SparkInflux;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.joda.time.DateTime;

import java.util.*;

public class JavaStreamingExample {
    public static void main(String[] args) {
        // Initialize Spark
        SparkConf conf = new SparkConf()
                .setMaster("local[*]")
                .setAppName("Example");
        JavaStreamingContext ssc = new JavaStreamingContext(conf, Durations.seconds(1));

        // Initialize SparkInflux instance
        SparkInflux sparkInflux = new SparkInflux("example", 1000);

        // Define tags for the point
        Map<String, String> tags = new HashMap<>();
        tags.put("t1", "A");
        tags.put("t2", "B");

        // Define fields for the point
        Map<String, Object> fields = new HashMap<>();
        fields.put("f1", 10.3);
        fields.put("f2", "x");
        fields.put("f3", -1);
        fields.put("f4", true);

        // Write a single point to "measurement1"
        Point point = new JavaPoint(
            DateTime.now(),
            "measurement1",
            tags,
            fields
        );

        // Create DStream of Influx points
        Queue<JavaRDD<Point>> queue = new LinkedList<>();

        // Add single RDD with a single Influx point to the DStream
        queue.add(ssc.sparkContext().parallelize(Collections.singletonList(point)));
        JavaDStream<Point> queueStream = ssc.queueStream(queue);

        // Save DStream to Influx
        sparkInflux.saveToInflux(queueStream);

        // Start Spark streaming
        ssc.start();
        ssc.awaitTermination();
    }
}
```

## Versioning explained

Version number (1.4.0.10.0.4.1-SNAPSHOT) of reactiveinflux-spark consists of four parts:

1. Apache Spark major and minor versions. (1.4)
2. InfluxDB major and minor versions. (0.10)
3. Reactiveinflux major and minor versions. (0.4)
4. Reactiveinflux-spark version. (1-SNAPSHOT)

Additionally Scala 2.11 and 2.10 versions are supported by adding "_2.11" or "_2.10" suffix to
the artifact name.