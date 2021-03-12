package com.dataintuitive.luciuscore
package model.v4

import genes._
import signatures._
import filters._

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
  case class Profiles(
    val fs:    String = "l1000",
    val t:     Option[Array[ValueVector]] = None,
    val p:     Option[Array[ValueVector]] = None,
    val r:     Option[Array[RankVector]]  = None,
    val logFc: Option[Array[ValueVector]] = None
  )

  sealed abstract class TRT(val trtType: String) extends Product with Serializable {
    val name: String
    val id: String
  }

  object PClass {
    val compoundLike = Set("trt_cp")
    val geneLike = Set("trt_lig")

    def isCompoundLike(trt:String) = compoundLike.contains(trt)
    def isGeneLike(trt:String) = geneLike.contains(trt)
  }

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
  ) extends TRT(trtType = "trt_cp") with Serializable

  case class TRT_LIG(
    name: String,
    id: String,
    dose: String,
    doseUnit: String,
    time: String,
    timeUnit: String
  ) extends TRT(trtType = "trt_lig") with Serializable

  // case class TRT_EMPTY() extends TRT(trtType = "empty") with Serializable

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
    ) extends TRT(trtType = "generic") with Serializable

  object TRT_EMPTY extends TRT_GENERIC(
    trtType = "empty",
    id = "noID",
    name = "noName",
    inchikey = None,
    smiles = None,
    pubchemId = None,
    dose = None,
    doseUnit = None,
    time = None,
    timeUnit = None
    ) with Serializable

  /**
   * Perturbation models a record in the database
   *
   * - profiles: A list of profiles can be stored in order to be able to use different
   * types of profiles: original l1000, inferred, ...
   */
  case class Perturbation(
    id: String,
    information: Information,
    profiles: List[Profiles],
    trtType: String,
    trt_generic: Option[TRT_GENERIC],
    trt_cp: Option[TRT_CP],
    trt_lig: Option[TRT_LIG],
    filters: Filters
  ) extends Serializable {

    def trt:TRT = trtType match {
      case "trt_cp"  => trt_cp.get.asInstanceOf[TRT_CP]
      case "trt_lig" => trt_lig.get.asInstanceOf[TRT_LIG]
      case _         => trt_generic.get
    }

    def trtSafe:TRT = trtType match {
      case "trt_cp"  if trt_cp.isDefined  => trt_cp.get.asInstanceOf[TRT_CP]
      case "trt_lig" if trt_lig.isDefined => trt_lig.get.asInstanceOf[TRT_LIG]
      case _  => trt_generic.get
    }

    def isEmpty:Boolean = trt_generic == Some(TRT_EMPTY)

  }

  import ModelFunctions._

  object Perturbation {
    def apply(
      id: String,
      info: Information = Information(),
      profiles: List[Profiles] = List(Profiles()),
      trt: TRT_GENERIC = TRT_EMPTY,
      filters: Filters = Nil
    ):Perturbation = trt.trtType match {
      case "trt_cp"  =>
        Perturbation(
          id,
          info,
          profiles,
          "trt_cp",
          None,
          Some(convertToSpecific(trt).asInstanceOf[TRT_CP]),
          None,
          filters
        )
      case "trt_lig" =>
        Perturbation(id,
         info,
         profiles,
         "trt_lig",
         None,
         None,
         Some(convertToSpecific(trt).asInstanceOf[TRT_LIG]),
         filters
       )
      case _ =>
        Perturbation(id,
         info,
         profiles,
         "empty",
         Some(trt),
         None,
         None,
         filters
       )
    }
  }

  object ModelFunctions {

    def convertToGeneric(trt:TRT):TRT_GENERIC =
      trt match {
        case t:TRT_GENERIC => t
        case t:TRT_CP =>
          TRT_GENERIC(
            "trt_cp",
            t.id,
            t.name,
            t.inchikey,
            t.smiles,
            t.pubchemId,
            Some(t.dose),
            Some(t.doseUnit),
            Some(t.time),
            Some(t.timeUnit)
          )
        case t:TRT_LIG =>
          TRT_GENERIC(
            "trt_lig",
            t.id,
            t.name,
            None,
            None,
            None,
            Some(t.dose),
            Some(t.doseUnit),
            Some(t.time),
            Some(t.timeUnit)
          )
      }

    def convertToSpecific(trt:TRT_GENERIC):TRT =
      trt.trtType match {
        case "trt_cp" =>
          TRT_CP(
            trt.name,
            trt.id,
            trt.dose.getOrElse("NA"),
            trt.doseUnit.getOrElse("NA"),
            trt.time.getOrElse("NA"),
            trt.timeUnit.getOrElse("NA"),
            trt.inchikey,
            trt.smiles,
            trt.pubchemId
          )
        case "trt_lig" =>
          TRT_LIG(
            trt.name,
            trt.id,
            trt.dose.getOrElse("NA"),
            trt.doseUnit.getOrElse("NA"),
            trt.time.getOrElse("NA"),
            trt.timeUnit.getOrElse("NA")
          )
      }
  }
}

object Model extends ModelTrait

