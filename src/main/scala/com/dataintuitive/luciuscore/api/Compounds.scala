package com.dataintuitive.luciuscore
package api

import model.v4._
import genes._
import lenses.CombinedPerturbationLenses._

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.SparkSession
import scala.collection.immutable.Map

object Compounds extends ApiFunctionTrait {

  case class SpecificData(
    compoundQuery: String,
    limit: Int
  )

  type JobOutput = Array[Map[String, String]]

  val infoMsg = s"Returns a list of compounds and corresponding samples matching a query, optionally with a limit on the number of results."

  val helpMsg =
    s"""
     |$infoMsg
     |
     |Input:
     |- query: Depending on the pattern, a regexp match or `startsWith` is applied (mandatory)
     |- version: v1, v2 or t1 (optional, default is `v1`)
     |- limit: The result size is limited to this number (optional, default is 10)
     |""".stripMargin

  def header(data: JobData) = s"Result for compound query ${data.specificData.compoundQuery}"

  def result(data: JobData)(implicit sparkSession: SparkSession) = {

    import sparkSession.implicits._

    val CachedData(db, _, genesDb) = data.cachedData
    val SpecificData(compoundQuery, limit) = data.specificData

    implicit val genes = genesDb

    // I could distinguish on version as well, but this makes more sense
    // This way, the same function can be reused for v1 and v2
    def isMatch(s: String, query: String): Boolean = {
      // Backward compatbility: Does query contains regexp or just first characters?
      val hasNonAlpha = compoundQuery.matches("^.*[^a-zA-Z0-9 ].*$")

      if (hasNonAlpha) s.matches(query)
      else s.startsWith(query)
    }

    val resultRDD =
      db.rdd.filter { p =>
          (isMatch(trtIdLens.get(p), compoundQuery)
          || isMatch( trtNameLens.get(p), compoundQuery))
        }
        .map { p =>
          (trtIdLens.get(p), trtNameLens.get(p))
        }
        .countByValue()
        .keys
        .toArray

    val resultRDDasMap = resultRDD
      .map {
        case (id, name) =>
          Map("compound_id" -> id, "name" -> name)
      }

    val resultRDDv1 = resultRDD
      .map { case (id, name) => (id, name) }

    val limitOutput = (resultRDD.length > limit)

    // Should we limit the result set?
    limitOutput match {
      case true  => resultRDDasMap.take(limit)
      case false => resultRDDasMap //.collect
    }

  }

}
