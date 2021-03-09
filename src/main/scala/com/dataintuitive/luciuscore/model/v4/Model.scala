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

  /**
   * The experimental conditions for the perturbation
   */
  case class Information(
    val batch: Option[String] = None,
    val plate: Option[String] = None,
    val well:  Option[String] = None,
    val cell:  Option[String] = None,
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

  sealed abstract class TRT(val trt_type: String) extends Product with Serializable {
    val name: String
  }

  object PClass {
    val compoundLike = Set("trt_cp")
    val geneLike = Set("trt_lig")

    def isCompoundLike(trt:String) = compoundLike.contains(trt)
    def isGeneLike(trt:String) = geneLike.contains(trt)
  }

  case class TRT_CP(
    name: String,
    concentration: String
  ) extends TRT(trt_type = "trt_cp") with Serializable

  case class TRT_LIG(
    name: String,
    probesetid: String
  ) extends TRT(trt_type = "trt_lig") with Serializable

  /**
   * Perturbation models a record in the database
   *
   * - profiles: A list of profiles can be stored in order to be able to use different
   * types of profiles: original l1000, inferred, ...
   */
  case class Perturbation(
    val id: String,
    val information: Information,
    val profiles: List[Profiles],
    val trt_type: String,
    val trt_cp: Option[TRT_CP],
    val trt_lig: Option[TRT_LIG]
  )

  object Perturbation {
    def apply(
      id: String,
      info: Information = Information(),
      profiles: List[Profiles] = List(Profiles()),
      trt: TRT
    ):Perturbation = trt match {
      case t:TRT_CP  => Perturbation(id, info, profiles, "trt_cp",  Some(t), None)
      case t:TRT_LIG => Perturbation(id, info, profiles, "trt_lig", None,    Some(t))
      case _         => Perturbation(id, info, profiles, "NA",      None,    None)
    }
  }
}
