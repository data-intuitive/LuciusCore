package com.dataintuitive.luciuscore
package api

import model.v4._
import AnnotatedIds._
import com.dataintuitive.luciuscore.TestData

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should._

import org.apache.spark.sql.Dataset
import org.apache.spark.sql.SparkSession

class AnnotatedIdsTest extends AnyFlatSpec with Matchers with TestData {

  val signature = List("MELK", "BRCA1")

  // Select the first 2 ids from the generated dataset
  val ids = testData.take(2).map(_.id).toList

}
