package com.dataintuitive.luciuscore
package api

import model.v4._
import genes._
import filters._
import signatures._
import correlations._
import utilities.BinningFunctions._

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.SparkSession

object BinnedCorrelation extends ApiFunctionTrait {

  case class SpecificData(
    signatureQuery: List[String],
    binsX: Int,
    binsY: Int,
    filters: Seq[(String, Seq[String])])

  type JobOutput = Seq[Map[String, Any]]

  val infoMsg = s"(Binned) 1D or 2D correlation values for plotting"

  val helpMsg =
    s"""
    |$infoMsg
    |""".stripMargin

  def header(data: JobData) = s"Binned correlations scores"

  def result(data: JobData)(implicit sparkSession: SparkSession) = {

    import sparkSession.implicits._

    val CachedData(db, _, genesDb) = data.cachedData
    val SpecificData(signatureQuery, binsX, binsY, filters) = data.specificData

    implicit val genes = genesDb
    val qfilters = filters.map{ case(key,values) => QFilter(key, values) }

    // TODO: Make sure we continue with all symbols, or just make the job invalid when it isn't!
    val signature = new SymbolSignature(signatureQuery.toArray)
    val iSignature = signature.toIndexSignature

    val vLength =
      db
        .filter( x =>
          x.profiles.profile.flatMap(_.t).isDefined)
        .first
        .profiles
        .profile
        .flatMap(_.t.map(_.length))
        .getOrElse(0)
    val query = iSignature.toOrderedRankVector(vLength)

    // Add Zhang score if signature is present
    // Filter as soon as possible
    val zhangAdded: Dataset[ScoredPerturbation] =
      db
        .filter(row => FilterFunctions.isMatch(qfilters, row.filters))
        .flatMap { DbFunctions.addScore(_, query) }

    val zhangStripped = zhangAdded.map(_.score).rdd

    if (binsY > 0)
      bin2D(zhangStripped, binsX, binsY)
    else
      bin1D(zhangStripped, binsX)

  }

}
