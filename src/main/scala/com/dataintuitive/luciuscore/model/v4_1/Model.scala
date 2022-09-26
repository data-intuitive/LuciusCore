package com.dataintuitive.luciuscore
package model.v4_1

import filters._
import model.v4.{TRT_GENERIC, Treatment}

case class InformationDetail(
  cell:  Option[String] = None,
  batch: Option[String] = None,
  plate: Option[String] = None,
  well:  Option[String] = None,
  year:  Option[String] = None,
  extra: Option[String] = None
) extends Serializable

object InformationDetailFields extends Enumeration {
  type InformationDetailFields = Value
  val Cell, Batch, Plate, Well, Year, Extra = Value
}

import InformationDetailFields._

/**
 * The experimental information for the perturbation
 */
case class Information(
  val processing_level: Int,
  val details: Seq[InformationDetail]
) extends Serializable {

  /**
    * Convert '|' separated string to a sequence of strings
    *
    * "abc|def|ghi" => Seq("abc", "def", "ghi")
    */
  def deserializeInfoField(value: String): Seq[String] = value.split("\\|", -1).toSeq
  def deserializeInfoField(value: Option[String]): Option[Seq[String]] = value.map(deserializeInfoField(_))

  /**
    * Convert a sequence of strings to a '|' separated string
    *
    * Seq("abc", "def", "ghi") => "abc|def|ghi"
    */
  def serializeInfoField(value: Seq[String]): String = value.mkString("|")
  def serializeInfoField(value: Option[Seq[String]]): Option[String] = value.map(serializeInfoField(_))

  /**
    * Expands a serialized details field to multiple deserialized Information fields
    * @param fixed Specifies which field to keep 'as-is'. This field won't be deserialized and instead be copied as-is to all new InformationDetails.
    * @return Information with deserialized details
    */
  def expandInformationDetail(fixed: Option[InformationDetailFields] = Some(InformationDetailFields.Cell)): Information = this.copy(
    details =
      this.details.flatMap{ id =>

        val cell = deserializeInfoField(id.cell)
        val batch = deserializeInfoField(id.batch)
        val plate = deserializeInfoField(id.plate)
        val well = deserializeInfoField(id.well)
        val year = deserializeInfoField(id.year)
        val extra = deserializeInfoField(id.extra)

        val replicates = Seq(cell, batch, plate, well, year, extra)
          .map( f => f.map(_.length).getOrElse(0) )
          .max

        (0 until replicates).map(i => fixed match {
          case Some(Cell)  => InformationDetail(id.cell,        batch.map(_(i)), plate.map(_(i)), well.map(_(i)), year.map(_(i)), extra.map(_(i)))
          case Some(Batch) => InformationDetail(cell.map(_(i)), id.batch,        plate.map(_(i)), well.map(_(i)), year.map(_(i)), extra.map(_(i)))
          case Some(Plate) => InformationDetail(cell.map(_(i)), batch.map(_(i)), id.plate,        well.map(_(i)), year.map(_(i)), extra.map(_(i)))
          case Some(Well)  => InformationDetail(cell.map(_(i)), batch.map(_(i)), plate.map(_(i)), id.well       , year.map(_(i)), extra.map(_(i)))
          case Some(Year)  => InformationDetail(cell.map(_(i)), batch.map(_(i)), plate.map(_(i)), well.map(_(i)), id.year,        extra.map(_(i)))
          case Some(Extra) => InformationDetail(cell.map(_(i)), batch.map(_(i)), plate.map(_(i)), well.map(_(i)), year.map(_(i)), id.extra       )
          case None        => InformationDetail(cell.map(_(i)), batch.map(_(i)), plate.map(_(i)), well.map(_(i)), year.map(_(i)), extra.map(_(i)))
        })
      }
  )

}

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
