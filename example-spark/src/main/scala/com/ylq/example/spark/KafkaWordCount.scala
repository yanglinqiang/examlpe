package com.ylq.example.spark

import java.util

import org.apache.kafka.clients.producer.{ProducerRecord, KafkaProducer, ProducerConfig}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Minutes, StreamingContext, Seconds}

/**
 * Class description goes here
 * Created by ylq on 15/8/25 上午10:22.
 */
object KafkaWordCount {
  def main(args: Array[String]) {
    //    if (args.length < 4) {
    //      System.err.println("Usage: KafkaWordCount <zkQuorum> <group> <topics> <numThreads>")
    //      System.exit(1)
    //    }
    //
    //    //    StreamingExamples.setStreamingLogLevels()
    //
    //    val Array(zkQuorum, group, topics, numThreads) = args
    val Array(zkQuorum, group, topics, numThreads) =
      Array("192.168.5.159:2181,192.168.5.158:2181", "testSpark", "EAGLEYE_LOG_CHANNEL1", "1")
    val sparkConf = new SparkConf()
      .setAppName("KafkaWordCount")
      .setMaster("local[1]")
    val ssc = new StreamingContext(sparkConf, Seconds(2))
    ssc.checkpoint("checkpoint")

    val topicMap = topics.split(",").map((_, numThreads.toInt)).toMap
    val lines = KafkaUtils.createStream(ssc, zkQuorum, group, topicMap).map(_._2)
    lines.print()
    val words = lines.flatMap(_.split(" "))
    words.print()
    val wordCounts = words.map(x => (x, 1L))
      .reduceByKeyAndWindow(_ + _, _ - _, Minutes(10), Seconds(2), 2)
    wordCounts.print()

    ssc.start()
    ssc.awaitTermination()
  }
}
