package com.dataintuitive.luciuscore
package model.v4

/**
 * Base class for a treatment
 *
 * This only contains an id and a name and some helper types and methods
 *
 * Concrete case classes extend this TRT class and add additional properties
 * For each case class we define a companion object that converts a TRT_GENERIC
 */
sealed abstract class TRT(val trtType: String) extends Product with Serializable {
  type T <: TRT
  val name: String
  val id: String
  val trt: T = this.asInstanceOf[T]
  def get():T = trt
}

object PClass {
  val compoundLike = Set("trt_cp")
  val geneLike = Set("trt_lig")

  def isCompoundLike(trt:String) = compoundLike.contains(trt)
  def isGeneLike(trt:String) = geneLike.contains(trt)
}

/**
 * TRT_CP class and companion object
 */
case class TRT_CP(
  name: String,
  id: String,
  dose: String,
  doseUnit: String,
  time: String,
  timeUnit: String,
  inchikey: Option[String],
  smiles: Option[String],
  pubchemId: Option[String]
) extends TRT(trtType = "trt_cp") with Serializable {

  type T = TRT_CP

}

object TRT_CP {

  def apply(generic:TRT_GENERIC):TRT_CP =
    TRT_CP(
      id = generic.id,
      name = generic.name,
      dose = generic.dose.getOrElse("NA"),
      doseUnit = generic.doseUnit.getOrElse("NA"),
      time = generic.time.getOrElse("NA"),
      timeUnit = generic.timeUnit.getOrElse("NA"),
      inchikey = generic.inchikey,
      smiles = generic.smiles,
      pubchemId = generic.pubchemId
    )

}

case class TRT_LIG(
  name: String,
  id: String,
  dose: String,
  doseUnit: String,
  time: String,
  timeUnit: String
) extends TRT(trtType = "trt_lig") with Serializable {

  type T = TRT_LIG

}

object TRT_LIG {

  def apply(generic: TRT_GENERIC):TRT_LIG =

    TRT_LIG(
      id = generic.id,
      name = generic.name,
      dose = generic.dose.getOrElse("NA"),
      doseUnit = generic.doseUnit.getOrElse("NA"),
      time = generic.time.getOrElse("NA"),
      timeUnit = generic.timeUnit.getOrElse("NA")
    )

}

case class TRT_GENERIC(
  override val trtType: String,
  id: String,
  name: String,
  inchikey: Option[String],
  smiles: Option[String],
  pubchemId: Option[String],
  dose: Option[String],
  doseUnit: Option[String],
  time: Option[String],
  timeUnit: Option[String]
) extends TRT(trtType = trtType) with Serializable {

  type T = TRT_GENERIC

  def toSpecific[S <: TRT]():S =
    trtType match {
      case "trt_cp" => TRT_CP(this).asInstanceOf[S]
      case "trt_lig" => TRT_LIG(this).asInstanceOf[S]
      case _ => this.asInstanceOf[S]
    }
}

object TRT_EMPTY extends TRT_GENERIC(
  trtType = "empty",
  id = "NA",
  name = "NA",
  inchikey = None,
  smiles = None,
  pubchemId = None,
  dose = None,
  doseUnit = None,
  time = None,
  timeUnit = None
) with Serializable

/**
  * Treatment contains the perturbation information
  *
  * There is a slot for every perturbagen/treatment type in order to
  * enable automatic encoding/decoding for Parquet and Spark DataSets.
  *
  * @param trt_generic
  * @param trt_cp
  * @param trt_lig
  */
case class Treatment(
  trt_generic:Option[TRT_GENERIC],
  trt_cp:Option[TRT_CP] = None,
  trt_lig:Option[TRT_LIG] = None
) extends Serializable {

  def specifics = Seq(trt_cp, trt_lig)
  def isSpecific = specifics.map(_.isDefined).filter(x=>x).length == 1
  def isConsistent = trt_generic.isDefined || isSpecific
  def isEmpty = (trt_generic == Some(TRT_EMPTY)) || (! isConsistent)

  def toSpecific =
    trt_generic.get.trtType match {
      case "trt_cp"    => Treatment(None, trt_cp  = trt_generic.map(_.toSpecific[TRT_CP]))
      case "trt_lig"   => Treatment(None, trt_lig = trt_generic.map(_.toSpecific[TRT_LIG]))
      case "trt_empty" => Treatment(None)
      case _ => Treatment(None)
    }

  def trtType:String = this match {
    case Treatment(x, None, None) => "trt_generic"
    case Treatment(None, x, None) => "trt_cp"
    case Treatment(None, None, x) => "trt_lig"
    case _ => "error"
  }

  def trt = trtSafe.getOrElse(TRT_EMPTY)
  def get = trt

  def trtSafe = this match {
    case Treatment(x, None, None) => x.map(_.trt)
    case Treatment(None, x, None) => x.map(_.trt)
    case Treatment(None, None, x) => x.map(_.trt)
    case _ => None
  }

}
