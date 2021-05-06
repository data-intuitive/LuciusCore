package com.dataintuitive.luciuscore
package api

import scala.collection.immutable.Map

import org.apache.spark.sql.Dataset
import org.apache.spark.sql.SparkSession

import genes.GenesDB
import utilities.SignedString
import model.v4._

object Statistics extends ApiFunctionTrait {

  case class SpecificData()

  type JobOutput = Map[String, Any]

  val infoMsg =
    "Return general statistics about the dataset.\nNo input is required. Pass null for parameters in Scala"

  val helpMsg =
    s"""
    |$infoMsg
    |
    |No input is required for this endpoint
    """.stripMargin

  def info(data: JobData) = "General statistics about the dataset"

  def header(data: JobData) = Map("key" -> "value").toString

  def result(data: JobData)(implicit sparkSession: SparkSession) = {

    import sparkSession.implicits._

    val CachedData(_, flatDb, _) = data.cachedData

    val compounds = Map(
      "total" -> flatDb
        .filter($"protocol" =!= "")
        .select($"compoundId")
        .distinct
        .count,
      "sample" -> flatDb
        .filter($"protocol" =!= "")
        .select($"compoundId")
        .distinct
        .take(10)
     )

    val samples = Map(
      "total" -> flatDb
        .filter($"protocol" =!= "")
        .select($"id")
        .distinct
        .count,
      "sample" -> flatDb
        .filter($"protocol" =!= "")
        .select($"id")
        .distinct
        .take(10)
    )

    val informative = Map(
      "total" -> flatDb
        .filter($"protocol" =!= "")
        .filter($"informative")
        .count
    )

    val concentrations = Map(
      "total" -> flatDb
        .filter($"concentration" =!= "")
        .select($"concentration")
        .distinct
        .count,
      "sample" -> flatDb
        .filter($"concentration" =!= "")
        .select($"concentration")
        .distinct
        .take(10)
    )

    val protocols = Map(
      "total" -> flatDb
        .filter($"protocol" =!= "")
        .select($"protocol")
        .distinct
        .count,
      "sample" -> flatDb
        .filter($"protocol" =!= "")
        .select($"protocol")
        .distinct
        .take(10)
    )

    val types = Map(
      "total" -> flatDb
        .filter($"compoundType" =!= "")
        .select($"compoundType")
        .distinct
        .count,
      "sample" -> flatDb
        .filter($"compoundType" =!= "")
        .select($"compoundType")
        .distinct
        .take(10)
    )

    Map(
      "samples" -> samples,
      "compounds" -> compounds,
      "informative" -> informative,
      "protocols" -> protocols,
      "types" -> types,
      "concentrations" -> concentrations
    )
  }
}
