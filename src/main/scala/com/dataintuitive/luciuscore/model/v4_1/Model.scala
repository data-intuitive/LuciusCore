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
  val processing_level: String,
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

        implicit class RichSeq(seq: Option[Seq[String]]) {
          def flatLift(i: Int): Option[String] = seq.flatMap(_.lift(i))
        }

        (0 until replicates).map(i => fixed match {
          case Some(Cell)  => InformationDetail(id.cell,          batch.flatLift(i), plate.flatLift(i), well.map(_(i)),   year.flatLift(i), extra.flatLift(i))
          case Some(Batch) => InformationDetail(cell.flatLift(i), id.batch,          plate.flatLift(i), well.flatLift(i), year.flatLift(i), extra.flatLift(i))
          case Some(Plate) => InformationDetail(cell.flatLift(i), batch.flatLift(i), id.plate,          well.flatLift(i), year.flatLift(i), extra.flatLift(i))
          case Some(Well)  => InformationDetail(cell.flatLift(i), batch.flatLift(i), plate.flatLift(i), id.well,          year.flatLift(i), extra.flatLift(i))
          case Some(Year)  => InformationDetail(cell.flatLift(i), batch.flatLift(i), plate.flatLift(i), well.flatLift(i), id.year,          extra.flatLift(i))
          case Some(Extra) => InformationDetail(cell.flatLift(i), batch.flatLift(i), plate.flatLift(i), well.flatLift(i), year.flatLift(i), id.extra         )
          case None        => InformationDetail(cell.flatLift(i), batch.flatLift(i), plate.flatLift(i), well.flatLift(i), year.flatLift(i), extra.flatLift(i))
        })
      }
  )

  /**
    * Check whether all values are alike between entries, eg. all cell values Some(String) or all cell values None
    * @return All is alike and normalized
    */
  def hasNormalizedInformationDetails(): Boolean = {
    val length = details.length
    val cells = details.map(_.cell)
    val batches = details.map(_.batch)
    val plates = details.map(_.plate)
    val wells = details.map(_.well)
    val years = details.map(_.year)
    val extras = details.map(_.extra)

    Seq(cells, batches, plates, wells, years, extras)
      .map(v => v.count(_.isDefined))
      .map(t => t == length || t == 0)
      .forall(b => b)
  }

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
  * Generic class to create a map-like structure of keys and values
  */
case class MetaInformation(key: String, value: String) extends Serializable

/**
 * Perturbation models a record in the database
 */
case class Perturbation(
  id: String,
  info: Information,
  profiles: Profiles,
  trtType: String,
  trt: Treatment,
  filters: Filters,
  meta: Seq[MetaInformation]
) extends Serializable

object Perturbation {
  def apply(
    id: String,
    info: Information,
    profiles: Profiles,
    trt: TRT_GENERIC,
    filters: Filters,
    meta: Seq[MetaInformation]
  ):Perturbation =
    Perturbation(
      id = id,
      info = info,
      profiles = profiles,
      trtType = trt.trtType,
      trt = Treatment(Some(trt)).toSpecific,
      filters = filters,
      meta = meta
    )
}

case class ScoredPerturbation(scores: List[Double], perturbation: Perturbation) extends Serializable {

    def score = scores.head

}

object ScoredPerturbation {

  def apply(score: Double, perturbation: Perturbation):ScoredPerturbation = ScoredPerturbation(List(score), perturbation)

}
