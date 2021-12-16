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

    val CachedData(_, flatDb, _, _) = data.cachedData

    val treatments = Map(
      "total" -> flatDb
        .filter($"cell" =!= "")
        .select($"treatmentId")
        .distinct
        .count,
      "sample" -> flatDb
        .filter($"cell" =!= "")
        .select($"treatmentId")
        .distinct
        .take(10)
     )

    val samples = Map(
      "total" -> flatDb
        .filter($"cell" =!= "")
        .select($"id")
        .distinct
        .count,
      "sample" -> flatDb
        .filter($"cell" =!= "")
        .select($"id")
        .distinct
        .take(10)
    )

    val informative = Map(
      "total" -> flatDb
        .filter($"cell" =!= "")
        .filter($"informative")
        .count
    )

    val doses = Map(
      "total" -> flatDb
        .filter($"dose" =!= "")
        .select($"dose")
        .distinct
        .count,
      "sample" -> flatDb
        .filter($"dose" =!= "")
        .select($"dose")
        .distinct
        .take(10)
    )

    val cells = Map(
      "total" -> flatDb
        .filter($"cell" =!= "")
        .select($"cell")
        .distinct
        .count,
      "sample" -> flatDb
        .filter($"cell" =!= "")
        .select($"cell")
        .distinct
        .take(10)
    )

    val types = Map(
      "total" -> flatDb
        .filter($"treatmentType" =!= "")
        .select($"treatmentType")
        .distinct
        .count,
      "sample" -> flatDb
        .filter($"treatmentType" =!= "")
        .select($"treatmentType")
        .distinct
        .take(10)
    )

    Map(
      "samples" -> samples,
      "treatments" -> treatments,
      "informative" -> informative,
      "cells" -> cells,
      "types" -> types,
      "doses" -> doses
    )
  }
}
