package com.dataintuitive.luciuscore
package api

import model.v4._
import Treatments._
import com.dataintuitive.luciuscore.model.v4.lenses.CombinedPerturbationLenses._
import com.dataintuitive.luciuscore.TestData

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should._

import org.apache.spark.sql.Dataset
import org.apache.spark.sql.SparkSession

class TreatmentsTest extends AnyFlatSpec with Matchers with TestData {

  import spark.implicits._

  // Create duplicate perturbation entries for every treatment
  val duplicateTreatmentData =
    testData
      .rdd
      .flatMap(p =>
          Seq(
            idLens.mod(id => id + "A", p),
            idLens.mod(id => id + "B", p))
          )
      .toDS

  def inputGenerator(specificData:Treatments.SpecificData): Treatments.JobData =
      Treatments.JobData(
        "version",
        CachedData(duplicateTreatmentData, flatData, genesDB, filters),
        specificData
      )

  "The result function" should "return as many entries as specified by the limit" in {

    val correctQuery = ".*"

    val input = inputGenerator(
      SpecificData(correctQuery, 10)
    )

    val result = Treatments.result(input)

    assert(result.size === 10)

  }

  it should "return an empty list when no matches" in {

    val correctQuery = "THIS___ENTRY_DOES___NOT_EXIST"

    val input = inputGenerator(
      SpecificData(correctQuery, 10)
    )

    val result = Treatments.result(input)

    assert(result.size === 0)

  }

  it should "return two perturbations for every treatment" in {

    val correctQuery = "LIG.*"

    val input = inputGenerator(
      SpecificData(correctQuery, 10)
    )

    val result = Treatments.result(input)

    assert(result.map(_("count").asInstanceOf[Long]).distinct.head === 2)
    assert(result.map(_("count")).distinct.size === 1)

  }

  it should "return only compound-like when like = compound" in {

    val inputNoMatch = inputGenerator(
      SpecificData("LIG.*", 10, List("compound"))
    )
    val inputMatch = inputGenerator(
      SpecificData("CP.*", 10, List("compound"))
    )

    assert(Treatments.result(inputNoMatch).length === 0)
    assert(Treatments.result(inputMatch).length === 10)

  }

  it should "return only genetic-like when like = genetic" in {

    val inputNoMatch = inputGenerator(
      SpecificData("CP.*", 10, List("genetic"))
    )
    val inputMatch = inputGenerator(
      SpecificData("LIG.*", 10, List("genetic"))
    )

    assert(Treatments.result(inputNoMatch).length === 0)
    assert(Treatments.result(inputMatch).length === 10)

  }

  it should "return only those entries with the correct type" in {

    val inputMatch = inputGenerator(
      SpecificData("CP.*", 10, Nil, List("trt_cp"))
    )
    val inputNoMatch = inputGenerator(
      SpecificData("LIG.*", 10, Nil, List("trt_cp"))
    )

    assert(Treatments.result(inputNoMatch).length === 0)
    assert(Treatments.result(inputMatch).length === 10)

  }

  it should "treat omit treatment id and just use name instead for genetic treatments" in {

    val inputMatch = inputGenerator(
      SpecificData(".*", 1, Nil, List("trt_lig"))
    )

    val id = Treatments.result(inputMatch).head.get("trtId")
    val name = Treatments.result(inputMatch).head.get("trtName")
    assert( id === name)

  }
}
