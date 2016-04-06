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
