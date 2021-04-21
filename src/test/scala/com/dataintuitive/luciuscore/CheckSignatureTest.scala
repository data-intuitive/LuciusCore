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
    val input = CheckSignature.JobData(testData, genesDB, "n/a", correctQuery)
    CheckSignature.result(input).head should contain ("symbol" -> "MELK")
  }

  it should "deal with faulty queries as well" in {
    val input = CheckSignature.JobData(testData, genesDB, "n/a", faultyQuery)
    CheckSignature.result(input).head should contain ("inL1000" -> false)
  }

  it should "deal with empty queries" in {
    val input = CheckSignature.JobData(testData, genesDB, "n/a", emptyQuery)
    CheckSignature.result(input) shouldBe empty
  }

}
