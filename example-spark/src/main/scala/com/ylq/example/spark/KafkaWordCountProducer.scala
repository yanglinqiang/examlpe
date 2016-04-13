//package com.ylq.example.spark

import java.util

import org.apache.kafka.clients.producer.{ProducerRecord, KafkaProducer, ProducerConfig}

/**
 * Class description goes here
 * Created by ylq on 15/8/26 上午11:15.
 */

// Produces some random words between 1 and 100.
object KafkaWordCountProducer {

  def main(args: Array[String]) {
    //    if (args.length < 4) {
    //      System.err.println("Usage: KafkaWordCountProducer <metadataBrokerList> <topic> " +
    //        "<messagesPerSec> <wordsPerMessage>")
    //      System.exit(1)
    //    }
    //
    //    val Array(brokers, topic, messagesPerSec, wordsPerMessage) = args
    val Array(brokers, topic, messagesPerSec, wordsPerMessage) =
      Array("192.168.5.159:9092,192.168.5.158:9092", "EAGLEYE_LOG_CHANNEL1", "1", "10")
    // Zookeeper connection properties
    val props = new util.HashMap[String, Object]()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringSerializer")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](props)
    //    Send some messages
    while (true) {
      (1 to messagesPerSec.toInt).foreach { messageNum =>
        val str = (1 to wordsPerMessage.toInt).map(x => scala.util.Random.nextInt(10).toString)
          .mkString(" ")
        System.out.println(str)
        val message = new ProducerRecord[String, String](topic, null, str)
        producer.send(message)
      }

      Thread.sleep(1000)
    }
  }

}

