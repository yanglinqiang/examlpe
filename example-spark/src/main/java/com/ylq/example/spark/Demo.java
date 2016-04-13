package com.ylq.example.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

/**
 * Class description goes here
 * Created by ylq on 15/8/19 下午3:41.
 */
public class Demo {
    public static void main(String[] args) {
        String logFile = "/Users/ylq/zhe800/project/imago/README.md"; // Should be some file on your system
        SparkConf conf = new SparkConf().setAppName("Simple Application")
                .setMaster("local[1]")
                .setJars(JavaSparkContext.jarOfClass(Demo.class));
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> logData = sc.textFile(logFile).cache();
        long numAs = logData.filter(new Function<String, Boolean>() {
            public Boolean call(String s) {
                return s.contains("a");
            }
        }).count();

        long numBs = logData.filter(new Function<String, Boolean>() {
            public Boolean call(String s) {
                return s.contains("b");
            }
        }).count();

        System.out.println("*******************************************Lines with a: " + numAs + ", lines with b: " + numBs);
    }
}
