package com.dataintuitive.luciuscore
package model.v4

import scala.reflect._

/**
    * Base trait for a treatment aka perturbagen
    */
  sealed abstract class TRT(val trtType: String) extends Product with Serializable {
    type S <: TRT
    val name: String
    val id: String
    val trt: S = this.asInstanceOf[S]

    // def get():S = this.asInstanceOf[S]
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
  ) extends TRT(trtType = "trt_cp") with Serializable { type S = TRT_CP }

  case class TRT_LIG(
    name: String,
    id: String,
    dose: String,
    doseUnit: String,
    time: String,
    timeUnit: String
  ) extends TRT(trtType = "trt_lig") with Serializable {

      type S = TRT_LIG
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

      type S = TRT_GENERIC

      def toSpecific[T <: TRT]():T =
        trtType match {
          case "trt_cp" =>
            TRT_CP(
              id,
              name,
              dose.getOrElse("NA"),
              doseUnit.getOrElse("NA"),
              time.getOrElse("NA"),
              timeUnit.getOrElse("NA"),
              inchikey,
              smiles,
              pubchemId
            ).asInstanceOf[T]
          case "trt_lig" =>
            TRT_LIG(
              id,
              name,
              dose.getOrElse("NA"),
              doseUnit.getOrElse("NA"),
              time.getOrElse("NA"),
              timeUnit.getOrElse("NA")
            ).asInstanceOf[T]
          case _ => this.asInstanceOf[T]
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

  case class Treatment(
    trt_generic:Option[TRT_GENERIC],
    trt_cp:Option[TRT_CP] = None,
    trt_lig:Option[TRT_LIG] = None
    ) extends Serializable {

    def isConsistent = trt_generic.isDefined || (trt_cp.isDefined && trt_lig.isDefined)

    def toSpecific =
        trt_generic.get.trtType match {
          case "trt_cp" => Treatment(None, trt_cp = trt_generic.map(_.toSpecific[TRT_CP]))
          case "trt_lig" => Treatment(None, trt_lig = trt_generic.map(_.toSpecific[TRT_LIG]))
          case _ => this
        }

    def trtType:String = this match {
      case Treatment(x, None, None) => "trt_generic"
      case Treatment(None, x, None) => "trt_cp"
      case Treatment(None, None, x) => "trt_lig"
      case _ => "error"
    }

    def trt = trtSafe.getOrElse(TRT_EMPTY)

    def trtSafe:Option[TRT] = this match {
      case Treatment(x, None, None) => x.map(_.asInstanceOf[TRT_GENERIC])
      case Treatment(None, x, None) => x.map(_.asInstanceOf[TRT_CP])
      case Treatment(None, None, x) => x.map(_.asInstanceOf[TRT_LIG])
      case _ => Some(TRT_EMPTY.asInstanceOf[TRT_GENERIC])
    }

    def isEmpty:Boolean = trt_generic == Some(TRT_EMPTY)

    }
