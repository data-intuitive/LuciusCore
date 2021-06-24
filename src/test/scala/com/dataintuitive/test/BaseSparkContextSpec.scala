package com.dataintuitive.test

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext

import org.apache.log4j.Logger
import org.apache.log4j.Level

object BaseSparkContextSpec {

  lazy val conf = new SparkConf()
    .setAppName("Test")
    .setMaster("local[*]")
  lazy val sc = new SparkContext(conf)

  lazy val sqlContext = new SQLContext(sc);
  lazy val spark = sqlContext.sparkSession

  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

}

trait BaseSparkContextSpec {

  lazy val sc = BaseSparkContextSpec.sc
  implicit lazy val spark = BaseSparkContextSpec.spark

}
