package com.dataintuitive.luciuscore
package api

import model.v4._
import CheckSignature._
import com.dataintuitive.luciuscore.TestData

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should._

import org.apache.spark.sql.Dataset
import org.apache.spark.sql.SparkSession

class CheckSignatureTest extends AnyFlatSpec with Matchers with TestData {

  val correctQuery = List("MELK", "BRCA1")
  val faultyQuery = List("MALK")
  val emptyQuery = Nil

  "The result function" should "simply work" in {
    val cData = CachedData(testData, flatData, genesDB, filters)
    val sData = SpecificData(correctQuery)
    val input = CheckSignature.JobData("version", cData, sData)
    CheckSignature.result(input).head should contain ("symbol" -> "MELK")
  }

  it should "deal with faulty queries as well" in {
    val cData = CachedData(testData, flatData, genesDB, filters)
    val sData = SpecificData(faultyQuery)
    val input = CheckSignature.JobData("version", cData, sData)
    CheckSignature.result(input).head should contain ("found" -> false)
  }

  it should "deal with empty queries" in {
    val cData = CachedData(testData, flatData, genesDB, filters)
    val sData = SpecificData(emptyQuery)
    val input = CheckSignature.JobData("version", cData, sData)
    CheckSignature.result(input) shouldBe empty
  }

}
