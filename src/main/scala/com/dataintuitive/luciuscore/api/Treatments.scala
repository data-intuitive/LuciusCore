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
    val isGeneticLike:Boolean
    val isLigandLike:Boolean
    def isLike: Like = (isCompoundLike, isGeneticLike, isLigandLike) match {
      case (true , false, false) => CompoundLike
      case (false, true , false) => GeneticLike
      case (false, false, true ) => LigandLike
      case _ => NoneLike
    }
  }

  case object CompoundLike extends Like {
    val isCompoundLike = true
    val isGeneticLike = false
    val isLigandLike = false
  }

  case object GeneticLike extends Like {
    val isCompoundLike = false
    val isGeneticLike = true
    val isLigandLike = false
  }

  case object LigandLike extends Like {
    val isCompoundLike = false
    val isGeneticLike = false
    val isLigandLike = true
  }

  case object NoneLike extends Like {
    val isCompoundLike = false
    val isGeneticLike = false
    val isLigandLike = false
  }

  def isLike(trtType: String):String = trtType match {
    case "trt_cp" => "compound"
    case "trt_lig" => "ligand"
    case "trt_sh" => "genetic"
    case "trt_oe" => "genetic"
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
    |Returns a list of treatments (and the amount of
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
     |
     |Be careful: for genetic treatments, the name corresponds to a gene/target,
     |but the id does not. There may be 10 entries in the database corresponding to gene MELK
     |and the combination id + MELK will still be unique. We have to adapt the logic of searching
     |and querying accordingly.
     |
     |This endpoint performs an aggregation for those treatment types that are genetic-like.
     |""".stripMargin

  def header(data: JobData) = s"Result for treatment query ${data.specificData.treatmentQuery}"

  def result(data: JobData)(implicit sparkSession: SparkSession) = {

    import sparkSession.implicits._

    val CachedData(db, _, genesDb, _) = data.cachedData
    val SpecificData(treatmentQuery, limit, like, trtType) = data.specificData

    implicit val genes = genesDb

    // I could distinguish on version as well, but this makes more sense
    // This way, the same function can be reused for v1 and v2
    def isMatch(s: String, query: String): Boolean = {
      // Backward compatbility: Does query contains regexp or just first characters?
      val hasNonAlpha = treatmentQuery.matches("^.*[^a-zA-Z0-9 -].*$")

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
        .map { p => {
          trtTypeLens.get(p) match {
            // TODO: Check if we can simply match on compound-like / genetic-like
            case "trt_lig" => (trtNameLens.get(p), trtNameLens.get(p), trtTypeLens.get(p))
            case "trt_sh" => (trtNameLens.get(p), trtNameLens.get(p), trtTypeLens.get(p))
            case "trt_oe" => (trtNameLens.get(p), trtNameLens.get(p), trtTypeLens.get(p))
            case _ => (trtIdLens.get(p), trtNameLens.get(p), trtTypeLens.get(p))
            }
          }
        }
        .countByValue()
        .toArray

    val resultRDDasMap = resultRDD
      .map {
        case ((id, name, trtType), count) =>
          Map(
            "trtId" -> id,
            "trtName" -> name,
            "count" -> count,
            "trtType" -> trtType
          )

      }

    val limitOutput = (resultRDD.length > limit)

    // Should we limit the result set?
    limitOutput match {
      case true  => resultRDDasMap.take(limit)
      case false => resultRDDasMap //.collect
    }

  }

}
