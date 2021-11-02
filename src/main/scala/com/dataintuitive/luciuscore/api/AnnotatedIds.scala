package com.dataintuitive.luciuscore
package api

import model.v4._
import genes._
import signatures._
import lenses.CombinedPerturbationLenses.lengthLens

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.SparkSession

object AnnotatedIds extends ApiFunctionTrait {

  case class SpecificData(
    signatureQuery: List[String],
    limit: Int,
    idsQuery: List[String],
    featuresQuery: List[String])

  type JobOutput = Array[Map[String, Any]]

  def header(data: JobData) = "Selected features: ${data.featuresQuery.toString}"

  val infoMsg = s"Annotations for a list of samples, optionally with zhang score"

  val helpMsg =
    s"""
    |$infoMsg
    |
    |Input:
    |
    |- query: signature or gene list for calculating Zhang scores (optional, no score is calculated if not provided)
    |- features: list of features to return with (optional, all features are returned if not provided)
    |- ids: list of pids to return annotations for (optional, some - see limit - pids are returned)
    |- limit: number of pwids to return if none are selected explicitly (optional, default is 10)
    |""".stripMargin

  def result(data: JobData)(implicit sparkSession: SparkSession) = {

    import sparkSession.implicits._

    val CachedData(db, _, genesDb, _) = data.cachedData
    val SpecificData(signatureQuery, limit, idsQuery, featuresQuery) = data.specificData
    implicit val genes = genesDb

    val signatureSpecified = !(signatureQuery.headOption.getOrElse(".*") == ".*")
    val idsSpecified = !(idsQuery.headOption.getOrElse(".*") == ".*")
    val featuresSpecified = !(featuresQuery.headOption.getOrElse(".*") == ".*")

    // TODO: Make sure we continue with all symbols, or just make the job invalid when it isn't!
    val signature = new SymbolSignature(signatureQuery.toArray)

    // Just taking the first element t vector is incorrect, what do we use options for after all?!
    // So... a version of takeWhile to the rescue
    val vLength = lengthLens.get(db.first)
    val query = signature.toIndexSignature.toOrderedRankVector(vLength)

    // Filter the pids in the query, at least if one is specified
    val filteredDb:Dataset[Perturbation] =
      if (idsSpecified)
        db.filter((p:Perturbation) =>
          idsQuery.map(q => p.id.matches(q)).reduce(_ || _))
      else
        db

    // Add Zhang score if signature is present
    val zhangAdded: Dataset[ScoredPerturbation] =
      if (signatureSpecified)
        filteredDb.flatMap{row => DbFunctions.addScore(row, query)}
      else
        filteredDb.map(ScoredPerturbation(0.0, _))

    // Should the output be limited? Only if no pwids or filter are specified
    val limitOutput: Boolean = !idsSpecified && (filteredDb.count > limit)

    val collected =
      if (!limitOutput)
        zhangAdded.collect
      else
        zhangAdded.take(limit)

    // Create a feature selection, depending on the input
    val features = {
      if (featuresSpecified) featuresQuery
      else
        List("zhang",
             "id",
             "jnjs",
             "jnjb",
             "smiles",
             "inchikey",
             "compoundname",
             "Type",
             "targets",
             "batch",
             "plateid",
             "well",
             "protocolname",
             "concentration",
             "year")
    }

    val result = collected
      .sortBy { sp => -sp.score }
      .map(entry => Extractors.ScoredPerturbationExtractor(entry, features))

    result.map(_.zip(features).map(_.swap).toMap)

  }

}
