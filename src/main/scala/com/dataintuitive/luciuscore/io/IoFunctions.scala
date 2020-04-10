package com.dataintuitive.luciuscore.io

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

/**
  * Created by toni on 28/04/16.
  */
object IoFunctions {

  /**
   * Helper function to remove quotes from a String
   */
  def removeQuotes(str: String) = if (str.startsWith("\"")) str.replace("\"", "") else str

  def loadTsv(sc: SparkContext,
              sampleCompoundAnnotationsFile: String,
              delimiter: String = "\t"):RDD[Array[String]] = {

    val rawRdd = sc.textFile(sampleCompoundAnnotationsFile)
    // The column headers are the first row split with the defined delimiter
    val header = rawRdd.first.split(delimiter).drop(1)
    // The rest of the data is handled similarly
    rawRdd
      .zipWithIndex
      .filter(_._2 > 0) // drop first row
      .map(_._1) // index not needed anymore
      .map(_.split(delimiter))
  }

}
