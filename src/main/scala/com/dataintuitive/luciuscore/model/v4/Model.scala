package com.dataintuitive.luciuscore
package model.v4

import filters._

/**
 * The experimental information for the perturbation
 */
case class Information(
  val cell:  Option[String] = None,
  val batch: Option[String] = None,
  val plate: Option[String] = None,
  val well:  Option[String] = None,
  val year:  Option[String] = None,
  val extra: Option[String] = None
) extends Serializable

/**
 * Container for the vectors connected to this perturbation
 *
 * The fs attribute (featureset) defines the type of vector and which translation
 * table to use.
 *
 * An entry for logFc is provided, but currently not yet used.
 *
 * Please note that we would use types if we could, but DataFrame encoding fails on it.
 */
case class Profile(
  val pType:  String = "",
  val length: Int = 0,
  val t:      Option[Array[Double]] = None,
  val p:      Option[Array[Double]] = None,
  val r:      Option[Array[Double]]  = None,
  val logFc:  Option[Array[Double]] = None
)

/** 
 *  case class for 1 or more profiles
 *
 *  We will mostly have just 1 profile, but allow for storing multiple
 *  Some convencience methods are defined
 */
case class Profiles(profiles:List[Profile] = Nil) {
  def isDefined = profiles.length > 0
  def profile:Option[Profile] = profile(0)
  def profile(i:Int):Option[Profile] = profiles.lift(i)
}

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
