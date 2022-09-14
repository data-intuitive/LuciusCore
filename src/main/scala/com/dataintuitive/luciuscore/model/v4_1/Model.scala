package com.dataintuitive.luciuscore
package model.v4_1

import filters._
import model.v4.{Treatment, TRT_GENERIC, Profiles}

/**
 * The experimental information for the perturbation
 */
case class Information(
  val processing_level: Int,
  val details: Seq[InformationDetail]
) extends Serializable

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
