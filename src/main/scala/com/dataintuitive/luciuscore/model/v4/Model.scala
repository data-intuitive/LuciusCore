package com.dataintuitive.luciuscore.model.v4

import com.dataintuitive.luciuscore.genes._
import com.dataintuitive.luciuscore.signatures._
import Filter._

/**
 * The types and classes used throughout Lucius.
 *
 * Be aware: The gene model and signature model are in separate packages.
 */
object Model extends Serializable {

  type RankVector = Array[Integer]
  type ValueVector = Array[Double]

  case class Information(
    val batch: Option[String] = None,
    val plate: Option[String] = None,
    val well:  Option[String] = None,
    val cell:  Option[String] = None,
    val year:  Option[String] = None,
    val extra: Option[String] = None
  )

  case class Profiles(
    val t:     Option[Array[ValueVector]] = None,
    val p:     Option[Array[ValueVector]] = None,
    val r:     Option[Array[RankVector]] = None,
    val logFc: Option[Array[ValueVector]] = None
  )

  sealed abstract class TRT(val trt_type: String) extends Product with Serializable {
    val name: String
  }

  case class TRT_CP(
    name: String,
    concentration: String
  ) extends TRT(trt_type = "trt_cp") with Serializable

  case class TRT_LIG(
    name: String,
    probesetid: String
  ) extends TRT(trt_type = "trt_lig") with Serializable

  case class Perturbation(
    val id: String,
    val information: Information,
    val profiles: Profiles,
    val inferredProfiles: Profiles,
    val trt_type: String,
    val trt_cp: Option[TRT_CP],
    val trt_lig: Option[TRT_LIG]
  )

  object Perturbation {
    def apply(
      id: String,
      info: Information = Information(),
      profiles: Profiles = Profiles(),
      inferredProfiles:Profiles = Profiles(),
      trt: TRT
    ):Perturbation = trt match {
      case t:TRT_CP  => Perturbation(id, info, profiles, inferredProfiles, "trt_cp", Some(t), None)
      case t:TRT_LIG => Perturbation(id, info, profiles, inferredProfiles, "trt_cp", None,    Some(t))
      case _         => Perturbation(id, info, profiles, inferredProfiles, "NA",     None,    None)
    }
  }
}
