package com.dataintuitive.luciuscore
package model.v4

import genes._
import signatures._
import filters._
import Treatment._

/**
 * The types and classes used throughout Lucius.
 *
 * Be aware: The gene model and signature model are in separate packages.
 */
trait ModelTrait extends Serializable {

  /**
   * The experimental conditions for the perturbation
   */
  case class Information(
    val cell:  Option[String] = None,
    val batch: Option[String] = None,
    val plate: Option[String] = None,
    val well:  Option[String] = None,
    val year:  Option[String] = None,
    val extra: Option[String] = None
  )

  /**
   * Container for the vectors connected to this perturbation
   *
   * The fs attribute (featureset) defines the type of vector and which translation
   * table to use.
   *
   * An entry for logFc is provided, but currently not yet used.
   */
  case class Profile(
    val pType:  String = "",
    val length: Int = 0,
    val t:      Option[Array[ValueVector]] = None,
    val p:      Option[Array[ValueVector]] = None,
    val r:      Option[Array[RankVector]]  = None,
    val logFc:  Option[Array[ValueVector]] = None
  )

  case class Profiles(profiles:List[Profile] = Nil) {
    def isDefined = profiles.length > 0
    def profile:Option[Profile] = profile(0)
    def profile(i:Int):Option[Profile] = profiles.lift(i)
  }

  /**
   * Perturbation models a record in the database
   *
   * - profiles: A list of profiles can be stored in order to be able to use different
   * types of profiles: original l1000, inferred, ...
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

}

object Model extends ModelTrait

