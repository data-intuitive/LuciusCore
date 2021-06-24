package com.dataintuitive.luciuscore
package api

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.SparkSession
import scala.collection.immutable.Map

import model.v4._
import genes._
import lenses.TreatmentLenses._

import Extractors._

object TargetToCompounds extends ApiFunctionTrait {

  case class SpecificData(
    targets: List[String],
    limit: Int
  )

  type JobOutput = Array[Map[String, Any]]

  val infoMsg = s"Returns a list of compounds matching a target query (list)."

  val helpMsg =
    s"""$infoMsg
        |
      | Input:
        | - query: List of targets to match (exact string match)
        | - version: v1, v2 or t1 (optional, default is `v1`)
     """.stripMargin

  def header(data:JobData):String = "All relevant data"

  def result(data:JobData)(implicit sparkSession: SparkSession):JobOutput = {

    val CachedData(db, _, genesDb) = data.cachedData
    val SpecificData(targetQuery, limit) = data.specificData
    implicit val genes = genesDb

    // I could distinguish on version as well, but this makes more sense
    // This way, the same function can be reused for v1 and v2
    def isMatch(targets: Seq[String], query:List[String]):Boolean = {
      // Exact match on one of the entries in the query
      targets.toList.map(target => query.toSet.contains(target)).foldLeft(false)(_||_)
    }

    val features = List(
      "compound_id",
      "compound_smiles",
      "compound_inchikey",
      "compound_name",
      "compound_type",
      "compound_targets"
    )


    // For now, restrict to trt_cp types
    val resultRDD =
      db.rdd
        .flatMap(_.trt.trt_cp)
        .filter{p =>
          isMatch(p.targets, targetQuery)
        }
        .distinct
        .sortBy(_.targets.size)

    val limitOutput = (resultRDD.count > limit)

    // Should we limit the result set?
    val result =
      limitOutput match {
        case true  => resultRDD.take(limit)
        case false => resultRDD.collect
      }

    result
      .map(entry => TreatmentExtractor(Treatment(Some(entry.toGeneric)), features) )
      .map(_.zip(features).map(_.swap).toMap)

  }

}
