package com.dataintuitive.luciuscore
package api.v4

import model.v4._
import Statistics._
import com.dataintuitive.luciuscore.TestData

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should._

import org.apache.spark.sql.Dataset
import org.apache.spark.sql.SparkSession

class StatisticsTest extends AnyFlatSpec with Matchers with TestData {

  val correctQuery = List("MELK", "BRCA1")
  val faultyQuery = List("MALK")
  val emptyQuery = Nil

  "The result function" should "simply work" in {
    val input = Statistics.JobData("version", CachedData(testData, flatData, genesDB, filters), SpecificData())
    Statistics.result(input).size shouldBe 6
  }

}
