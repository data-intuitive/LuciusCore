package com.dataintuitive.luciuscore
package model.v4

/**
 * Base class for a treatment
 *
 * This only contains an id and a name and some helper types and methods
 *
 * Concrete case classes extend this TRT class and add additional properties
 * For each case class we define a companion object that converts a TRT_GENERIC
 *
 * Not all types have been implemented yet.
 * An overview of all the possible perturbagen types can be found here:
 * https://clue.io/connectopedia/perturbagen_types_and_controls
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
  val geneLike = Set("trt_lig", "trt_sh")

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
  pubchemId: Option[String],
  targets: List[String]
) extends TRT(trtType = "trt_cp") with Serializable {

  type T = TRT_CP

  def toGeneric:TRT_GENERIC =
    TRT_GENERIC(
      trtType = "trt_cp",
      id = id,
      name = name,
      inchikey = inchikey,
      smiles = smiles,
      pubchemId = pubchemId,
      dose = Some(dose),
      doseUnit = Some(doseUnit),
      time = Some(time),
      timeUnit = Some(timeUnit),
      targets = Some(targets)
    )

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
      pubchemId = generic.pubchemId,
      targets = generic.targets.getOrElse(Nil)
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

  def toGeneric:TRT_GENERIC =
    TRT_GENERIC(
      trtType = "trt_lig",
      id = id,
      name = name,
      inchikey = None,
      smiles = None,
      pubchemId = None,
      dose = Some(dose),
      doseUnit = Some(doseUnit),
      time = Some(time),
      timeUnit = Some(timeUnit),
      targets = None
    )

}

case class TRT_SH(
  name: String,
  id: String
) extends TRT(trtType = "trt_sh") with Serializable {

  type T = TRT_SH

  def toGeneric:TRT_GENERIC =
    TRT_GENERIC(
      trtType = "trt_sh",
      id = id,
      name = name,
      inchikey = None,
      smiles = None,
      pubchemId = None,
      dose = None,
      doseUnit = None,
      time = None,
      timeUnit = None,
      targets = None
    )

}

object TRT_SH {

  def apply(generic: TRT_GENERIC):TRT_SH =

    TRT_SH(
      id = generic.id,
      name = generic.name
    )

}

case class CTL_VECTOR(
  name: String,
  id: String
) extends TRT(trtType = "ctl_vector") with Serializable {

  type T = CTL_VECTOR

  def toGeneric:TRT_GENERIC =
    TRT_GENERIC(
      trtType = "ctl_vector",
      id = id,
      name = name,
      inchikey = None,
      smiles = None,
      pubchemId = None,
      dose = None,
      doseUnit = None,
      time = None,
      timeUnit = None,
      targets = None
    )

}

object CTL_VECTOR {

  def apply(generic: TRT_GENERIC):CTL_VECTOR =

    CTL_VECTOR(
      id = generic.id,
      name = generic.name
    )

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
  timeUnit: Option[String],
  targets: Option[List[String]]
) extends TRT(trtType = trtType) with Serializable {

  type T = TRT_GENERIC

  def toSpecific[S <: TRT]():S =
    trtType match {
      case "trt_cp"     => TRT_CP(this).asInstanceOf[S]
      case "trt_lig"    => TRT_LIG(this).asInstanceOf[S]
      case "trt_sh"     => TRT_SH(this).asInstanceOf[S]
      case "ctl_vector" => CTL_VECTOR(this).asInstanceOf[S]
      case _ => this.asInstanceOf[S]
    }

  def toGeneric:TRT_GENERIC = this

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
  timeUnit = None,
  targets = None
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
  trt_lig:Option[TRT_LIG] = None,
  trt_sh:Option[TRT_SH] = None,
  ctl_vector:Option[CTL_VECTOR] = None
) extends Serializable {

  // Which specific treatment types are supported?
  def specifics = Seq(trt_cp, trt_lig, trt_sh, ctl_vector)
  def isSpecific = specifics.map(_.isDefined).filter(x=>x).length == 1
  def isConsistent = trt_generic.isDefined || isSpecific
  def isEmpty = (trt_generic == Some(TRT_EMPTY)) || (! isConsistent)

  // Convert to a specific treatment type (access slot in Treatment)
  def toSpecific:Treatment =
    trt_generic.get.trtType match {
      case "trt_cp"     => Treatment(None, trt_cp  = trt_generic.map(_.toSpecific[TRT_CP]))
      case "trt_lig"    => Treatment(None, trt_lig = trt_generic.map(_.toSpecific[TRT_LIG]))
      case "trt_sh"     => Treatment(None, trt_sh  = trt_generic.map(_.toSpecific[TRT_SH]))
      case "ctl_vector" => Treatment(None, ctl_vector = trt_generic.map(_.toSpecific[CTL_VECTOR]))
      case "trt_empty"  => Treatment(None)
      case _ => Treatment(None)
    }

  // Convert to a generic treatment type (inverse of toSpecific)
  def toGeneric:Treatment = trtType match {
    case "trt_generic" => this
    case "trt_cp"      => Treatment(trt_cp.map(_.toGeneric))
    case "trt_lig"     => Treatment(trt_lig.map(_.toGeneric))
    case "trt_sh"      => Treatment(trt_sh.map(_.toGeneric))
    case "ctl_vector"  => Treatment(ctl_vector.map(_.toGeneric))
    case _ =>  Treatment(Some(TRT_EMPTY))
  }

  def trtType:String = this match {
    case Treatment(x, None, None, None, None) => "trt_generic"
    case Treatment(None, x, None, None, None) => "trt_cp"
    case Treatment(None, None, x, None, None) => "trt_lig"
    case Treatment(None, None, None, x, None) => "trt_sh"
    case Treatment(None, None, None, None, x) => "ctl_vector"
    case _ => "error"
  }

  def trt = trtSafe.getOrElse(TRT_EMPTY)
  def get = trt

  def trtSafe = this match {
    case Treatment(x, None, None, None, None) => x.map(_.trt.asInstanceOf[TRT_GENERIC])
    case Treatment(None, x, None, None, None) => x.map(_.trt.asInstanceOf[TRT_CP])
    case Treatment(None, None, x, None, None) => x.map(_.trt.asInstanceOf[TRT_LIG])
    case Treatment(None, None, None, x, None) => x.map(_.trt.asInstanceOf[TRT_SH])
    case Treatment(None, None, None, None, x) => x.map(_.trt.asInstanceOf[CTL_VECTOR])
    case _ => None
  }

  def dose:Option[String] = trtType match {
    case "trt_generic" => trt_generic.flatMap(_.dose)
    case "trt_cp"      => trt_cp.map(_.dose)
    case "trt_lig"     => trt_lig.map(_.dose)
    case _ => None
  }

  def doseUnit:Option[String] = trtType match {
    case "trt_generic" => trt_generic.flatMap(_.doseUnit)
    case "trt_cp"      => trt_cp.map(_.doseUnit)
    case "trt_lig"     => trt_lig.map(_.doseUnit)
    case _ => None
  }

  def time:Option[String] = trtType match {
    case "trt_generic" => trt_generic.flatMap(_.time)
    case "trt_cp"      => trt_cp.map(_.time)
    case "trt_lig"     => trt_lig.map(_.time)
    case _ => None
  }

  def timeUnit:Option[String] = trtType match {
    case "trt_generic" => trt_generic.flatMap(_.timeUnit)
    case "trt_cp"      => trt_cp.map(_.timeUnit)
    case "trt_lig"     => trt_lig.map(_.timeUnit)
    case _ => None
  }

  def inchikey:Option[String] = trtType match {
    case "trt_generic" => trt_generic.flatMap(_.inchikey)
    case "trt_cp"      => trt_cp.flatMap(_.inchikey)
    case _ => None
  }

  def smiles:Option[String] = trtType match {
    case "trt_generic" => trt_generic.flatMap(_.smiles)
    case "trt_cp"      => trt_cp.flatMap(_.smiles)
    case _ => None
  }

  def pubchemId:Option[String] = trtType match {
    case "trt_generic" => trt_generic.flatMap(_.pubchemId)
    case "trt_cp"      => trt_cp.flatMap(_.pubchemId)
    case _ => None
  }

}
