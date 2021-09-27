package com.dataintuitive.luciuscore
package api

import model.v4._
import genes._
import lenses.CombinedPerturbationLenses._

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.SparkSession
import scala.collection.immutable.Map

object Treatments extends ApiFunctionTrait {

  trait Like extends Serializable {

      val isCompoundLike:Boolean
      def isGeneticLike = !isCompoundLike
      def isLike:Like = if (isCompoundLike) CompoundLike else GeneticLike

  }

  case object CompoundLike extends Like {
    val isCompoundLike = true

  }

  case object GeneticLike extends Like {
      val isCompoundLike = false
  }

  case object NoneLike extends Like {
      val isCompoundLike = false
  }

  // def isLike(trt:TRT):Like = trtTypeLens(trt) match {
  //     case "trt_cp" => CompoundLike
  //     case "trt_lig" => GeneticLike
  //     case "trt_sh" => GeneticLike
  //     case _ => NoneLike
  // }

  def isLike(trtType: String):String = trtType match {
      case "trt_cp" => "compound"
      case "trt_lig" => "genetic"
      case "trt_sh" => "genetic"
      case _ => "none"
  }

  case class SpecificData(
    treatmentQuery: String,
    limit: Int,
    like: List[String] = Nil,
    trtType: List[String] = Nil
  )

  type JobOutput = Array[Map[String, String]]

  val infoMsg =
    """
    |Returns a list of perturbations (and the amount of
    |samples/perturbations corresponding to it),
    |optionally with a limit on the number of results.
    |""".stripMargin

  val helpMsg =
    s"""
     |$infoMsg
     |
     |Input:
     |- query: Depending on the pattern, a regexp match or `startsWith` is applied (mandatory)
     |- limit: The result size is limited to this number (optional, default is 10)
     |- like: `compound` for compound-like, `genetic` (optional, default is both)
     |- trtType: a specific treatment type (optional, default is "*")
     |""".stripMargin

  def header(data: JobData) = s"Result for treatment query ${data.specificData.treatmentQuery}"

  def result(data: JobData)(implicit sparkSession: SparkSession) = {

    import sparkSession.implicits._

    val CachedData(db, _, genesDb) = data.cachedData
    val SpecificData(treatmentQuery, limit, like, trtType) = data.specificData

    implicit val genes = genesDb

    // I could distinguish on version as well, but this makes more sense
    // This way, the same function can be reused for v1 and v2
    def isMatch(s: String, query: String): Boolean = {
      // Backward compatbility: Does query contains regexp or just first characters?
      val hasNonAlpha = treatmentQuery.matches("^.*[^a-zA-Z0-9 ].*$")

      if (hasNonAlpha) s.matches(query)
      else s.startsWith(query)
    }

    val trtTypeSpecified = (trtType.length > 0)
    val likeSpecified = (like.length > 0)

    val resultRDD =
      db.rdd.filter { p =>
          (isMatch(trtIdLens.get(p), treatmentQuery)
          || isMatch( trtNameLens.get(p), treatmentQuery))
        }
        .filter{ p =>
          if (trtTypeSpecified)
            trtType.contains(trtTypeLens.get(p))
          else
            true
        }
        .filter{ p =>
          if (likeSpecified)
            like.contains(isLike(trtTypeLens.get(p)))
          else
            true
        }
        .map { p =>
          (trtIdLens.get(p), trtNameLens.get(p), trtTypeLens.get(p))
        }
        .countByValue()
        .toArray

    val resultRDDasMap = resultRDD
      .map {
        case ((id, name, trtType), count) =>
          Map("trtId" -> id, "trtName" -> name, "count" -> count, "trtType" -> trtType)
      }

    val limitOutput = (resultRDD.length > limit)

    // Should we limit the result set?
    limitOutput match {
      case true  => resultRDDasMap.take(limit)
      case false => resultRDDasMap //.collect
    }

  }

}
