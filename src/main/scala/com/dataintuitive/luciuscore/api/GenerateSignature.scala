package com.dataintuitive.luciuscore
package api

import model.v4._
import genes._
import lenses.CombinedPerturbationLenses._

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.SparkSession
import scala.collection.immutable.Map

object GenerateSignature extends ApiFunctionTrait {

  case class SpecificData(
    pValue: Double,
    perturbationQuery: List[String]
  )

  type JobOutput = Array[Map[String, String]]

  val infoMsg = s""

  val helpMsg =
    s"""
     |$infoMsg
     |""".stripMargin

  def header(data: JobData) = s"Result for perturbation query ${data.specificData.perturbationQuery}"

  def result(data: JobData)(implicit sparkSession: SparkSession) = {

    import sparkSession.implicits._

    val CachedData(db, _, genesDb) = data.cachedData
    val SpecificData(pValue, pQuery) = data.specificData

    implicit val genes = genesDb

    // Start with query compound and retrieve indices
    val selection =
      db.filter(x => pQuery.toSet.contains(x.id))
        .collect
        .map(x => (x.profiles.profile.flatMap(_.t).get, x.profiles.profile.flatMap(_.p).get))
    val valueVector = TransformationFunctions.aggregateStats(selection, pValue)
    val rankVector = TransformationFunctions.stats2RankVector((valueVector, Array()))

    val indexSignature: signatures.IndexSignature =
      TransformationFunctions.rankVector2IndexSignature(rankVector)

    val symbolSignature = indexSignature.toSymbolSignature

    symbolSignature.toArray

  }

}
