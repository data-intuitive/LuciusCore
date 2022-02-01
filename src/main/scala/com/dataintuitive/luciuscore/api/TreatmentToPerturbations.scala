package com.dataintuitive.luciuscore
package api

import model.v4._
import lenses.CombinedPerturbationLenses._
import genes._
import Extractors._

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.SparkSession
import scala.collection.immutable.Map

object TreatmentToPerturbations extends ApiFunctionTrait {

  case class SpecificData(
                     pValue: Double,
                     compounds: List[String],
                     limit: Int)

  type JobOutput = Array[Map[String, Any]]

  val infoMsg = "Returns a list of samples matching a compound query (list)."

  val helpMsg =
    s"""
    |$infoMsg
    |
    |Input:
    |- query: List of compounds jnj to match (exact string match)
    |- version: v1, v2 or t1 (optional, default is `v1`)
    |""".stripMargin

  def header(data: JobData) = s"Result for compound query ${data.specificData.compounds}"

  def result(data: JobData)(implicit sparkSession: SparkSession) = {

    val CachedData(db, _, genesDb, _) = data.cachedData
    val SpecificData(pValue, treatmentQuery, limit) = data.specificData
    implicit val genes = genesDb

    // I could distinguish on version as well, but this makes more sense
    // This way, the same function can be reused for v1 and v2
    def isMatch(s: String, query: List[String]): Boolean = {
      // Exact match on one of the entries in the query
      query.toSet.contains(s)
    }

    // TODO: Check if we can simply match on compound-like / genetic-like
    val result =
      db.filter { p => trtTypeLens.get(p) match {
          case "trt_lig" => isMatch(trtNameLens.get(p), treatmentQuery)
          case "trt_sh" => isMatch(trtNameLens.get(p), treatmentQuery)
          case "trt_oe" => isMatch(trtNameLens.get(p), treatmentQuery)
          case _ => isMatch(trtIdLens.get(p), treatmentQuery)
          }
        }
        .collect
        .map(entry => PerturbationExtractor(entry, PerturbationExtractor.allFeatures, pValue))

    result.map(_.zip(PerturbationExtractor.allFeatures).map(_.swap).toMap)

  }

}

