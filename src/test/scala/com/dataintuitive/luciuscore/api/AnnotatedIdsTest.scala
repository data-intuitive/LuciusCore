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

val input =api. AnnotatedIds.JobData(data, genesDB, "1.1", List("MELK", "BRCA1"), 10, List("A375_24H_BRD_K19876534_3.32999992371"), List("zhang", "id", "pid"))
  val correctQuery = List("MELK", "BRCA1")
  val faultyQuery = List("MALK")
  val emptyQuery = Nil

  "The result function" should "simply work" in {
    val cData = CachedData(testData, flatData, genesDB)
    val sData = SpecificData(correctQuery)
    val input = CheckSignature.JobData("version", cData, sData)
    CheckSignature.result(input).head should contain ("symbol" -> "MELK")
  }

  it should "deal with faulty queries as well" in {
    val cData = CachedData(testData, flatData, genesDB)
    val sData = SpecificData(faultyQuery)
    val input = CheckSignature.JobData("version", cData, sData)
    CheckSignature.result(input).head should contain ("inL1000" -> false)
  }

  it should "deal with empty queries" in {
    val cData = CachedData(testData, flatData, genesDB)
    val sData = SpecificData(emptyQuery)
    val input = CheckSignature.JobData("version", cData, sData)
    CheckSignature.result(input) shouldBe empty
  }

}
