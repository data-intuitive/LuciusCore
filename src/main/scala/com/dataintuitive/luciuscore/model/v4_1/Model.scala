package com.dataintuitive.luciuscore
package model.v4_1

import filters._
import model.v4.{Treatment, TRT_GENERIC, Profiles}
import model.v4_1.lenses.InformationLenses._

/**
 * The experimental information for the perturbation
 */
case class Information(
  val processing_level: Int,
  val details: Seq[InformationDetail]
) extends Serializable {
  def toExpanded(): Information = {
    // Helper function to determine how many fields there are
    def getLength(str: Option[String]) = {
      str match {
        case None => 0
        case Some(s) if s.isEmpty => 0
        case Some(s) => s.count(_ == '|') + 1
      }
    }

    val newLength = Math.max(
      Math.max(
        Math.max(getLength(serializedCellLens.get(this)), getLength(serializedBatchLens.get(this))),
        Math.max(getLength(serializedPlateLens.get(this)), getLength(serializedWellLens.get(this)))
      ),
      Math.max(getLength(serializedYearLens.get(this)), getLength(serializedExtraLens.get(this)))
    )
    val resizedInfo = replicatesLens.set(this, Some(newLength))
    val populationCell = serializedCellLens.set(resizedInfo, serializedCellLens.get(this))
    val populatedBatch = serializedBatchLens.set(populationCell, serializedBatchLens.get(this))
    val populatedPlate = serializedPlateLens.set(populatedBatch, serializedPlateLens.get(this))
    val populatedWell = serializedWellLens.set(populatedPlate, serializedWellLens.get(this))
    val populatedYear = serializedYearLens.set(populatedWell, serializedYearLens.get(this))
    val populatedExtra = serializedExtraLens.set(populatedYear, serializedExtraLens.get(this))
    populatedExtra
  }
}

case class InformationDetail(
  val cell:  Option[String] = None,
  val batch: Option[String] = None,
  val plate: Option[String] = None,
  val well:  Option[String] = None,
  val year:  Option[String] = None,
  val extra: Option[String] = None
) extends Serializable

/**
 * Perturbation models a record in the database
 */
case class Perturbation(
  id: String,
  info: Information,
  profiles: Profiles,
  trtType: String,
  trt: Treatment,
  filters: Filters
) extends Serializable

object Perturbation {
  def apply(
    id: String,
    info: Information,
    profiles: Profiles,
    trt: TRT_GENERIC,
    filters: Filters
  ):Perturbation =
    Perturbation(
      id = id,
      info = info,
      profiles = profiles,
      trtType = trt.trtType,
      trt = Treatment(Some(trt)).toSpecific,
      filters = filters
    )
}

case class ScoredPerturbation(scores: List[Double], perturbation: Perturbation) extends Serializable {

    def score = scores.head

}

object ScoredPerturbation {

  def apply(score: Double, perturbation: Perturbation):ScoredPerturbation = ScoredPerturbation(List(score), perturbation)

}
