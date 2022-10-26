package com.dataintuitive.luciuscore
package api.v4_1

import model.v4_1._
import lenses.CombinedPerturbationLenses._
import genes._
import Extractors._

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.SparkSession
import scala.collection.immutable.Map

object PerturbationsInformationDetails extends ApiFunctionTrait {

  case class SpecificData(
                     perturbationId: String
                    )

  type JobOutput = Array[Map[String, Any]]

  val infoMsg = "Returns information details for a specified perturbation."

  val helpMsg =
    s"""
    |$infoMsg
    |
    |Input:
    |- perturbationId: Id for which perturbation to get the information details
    |""".stripMargin

  def header(data: JobData) = s"Result for perturbation information details query ${data.specificData.perturbationId}"

  def result(data: JobData)(implicit sparkSession: SparkSession) = {

    val CachedData(db, _, genesDb, _) = data.cachedData
    val SpecificData(perturbationId) = data.specificData
    implicit val genes = genesDb

    val perturbation =
      db.filter { p => p.id == perturbationId}
        .collect
        .head

    val result = PerturbationExtractor(perturbation, PerturbationExtractor.informationDetails, 0)
    PerturbationExtractor.informationDetails.zip(result).toMap
  }

}

