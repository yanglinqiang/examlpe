package com.ylq.example.spark

import org.apache.spark.streaming.{Seconds, Minutes}

/**
 * Class description goes here
 * Created by ylq on 15/8/25 上午10:16.
 */
object KafkaApp {
  def main(args: Array[String]) {
    //    val str = (1 to 10).map(x => scala.util.Random.nextInt(10).toString).mkString
    val str = "name name name name name name"
    System.out.println(str)
    val res = str.split(" ").map(x => (x, 1))
    System.out.println(res)
  }
}
