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
