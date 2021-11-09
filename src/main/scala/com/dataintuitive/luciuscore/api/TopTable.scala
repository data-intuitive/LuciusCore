package com.dataintuitive.luciuscore
package api

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.SparkSession

import model.v4._
import genes._
import filters._
import signatures._
import correlations._
import lenses.CombinedPerturbationLenses.lengthLens

object TopTable extends ApiFunctionTrait {

  case class SpecificData(
    head: Int,
    tail: Int,
    signatureQuery: List[String],
    featuresQuery: List[String],
    filters: Seq[(String, Seq[String])]
  )

  type JobOutput = Array[Map[String, Any]]

  def header(data: JobData) = s"Selected features: ${data.specificData.featuresQuery.toString}"

  val infoMsg = s"Top Table wrt Zhang Score"

  val helpMsg =
    s"""
    |$infoMsg
    |
    |- head: number of entries to return for a toptable
    |- tail: if head=0, number of entries to return for bottom table
    |- query: signature (or gene list) for calculating Zhang scores
    |- features: list of features to return with (optional, all features are returned if not provided)
    |- filters: The filters to apply
    |""".stripMargin

  def result(data: JobData)(implicit sparkSession: SparkSession) = {

    val CachedData(db, _, genesDb, _) = data.cachedData
    val SpecificData(head, tail, signatureQuery, featuresQuery, filters) = data.specificData

    implicit val genes = genesDb

    val signatureSpecified = !(signatureQuery.headOption.getOrElse(".*") == ".*")
    val featuresSpecified = !(featuresQuery.headOption.getOrElse(".*") == ".*")

    val qfilters = filters.map{ case(key,values) => QFilter(key, values) }

    // TODO: Make sure we continue with all symbols, or just make the job invalid when it isn't!
    val signature = new SymbolSignature(signatureQuery.toArray)
    val iSignature = signature.toIndexSignature

    // Just taking the first element t vector is incorrect, what do we use options for after all?!
    // So... a version of takeWhile to the rescue
    val vLength = lengthLens.get(db.first)
    val query = iSignature.toOrderedRankVector(vLength)

    // Calculate Zhang score for all entries that contain a rank vector
    // This should be used in a flatMap
    def updateZhang(x: Perturbation, query: Array[Double]): Option[ScoredPerturbation] = {
      x.profiles.profile.flatMap(_.r) match {
        case Some(r) => Some(ScoredPerturbation(ZhangScoreFunctions.connectionScore(r, query), x))
        case _       => None
      }
    }

    // Add Zhang score if signature is present
    // Filter as soon as possible
    val zhangAdded: RDD[ScoredPerturbation] =
      db.rdd
        .filter(row => FilterFunctions.isMatch(qfilters, row.filters))
        .flatMap { updateZhang(_, query) }

    val topN =
      if (head > 0) {
        implicit val descendingOrdering = Ordering.by((sp:ScoredPerturbation) => -sp.score)
        zhangAdded
          .takeOrdered(head)
      } else {
        implicit val descendingOrdering = Ordering.by((sp:ScoredPerturbation) => sp.score)
        zhangAdded
          .takeOrdered(tail)
      }

    // Create a feature selection, depending on the input
    // TODO: Add target information and make sure it gets parsed correctly!
    val features = {
      if (featuresSpecified) featuresQuery
      else
        List(
           "id",
           "zhang",
           "batch",
           "plate",
           "well",
           "cell",
           "dose",
           "year",
           "time",
           "trt",
           "trt_id",
           "trt_name",
           "smiles",
           "inchikey",
           "targets",
           "filters"
       )
    }

    val result =
      if (head > 0)
        topN
          .sortBy((sp:ScoredPerturbation) => -sp.score)
          .map(entry => Extractors.ScoredPerturbationExtractor(entry, features))
      else
        topN
          .sortBy((sp:ScoredPerturbation) => sp.score)
          .map(entry => Extractors.ScoredPerturbationExtractor(entry, features))

    result.map(_.zip(features).map(_.swap).toMap)

  }

}
