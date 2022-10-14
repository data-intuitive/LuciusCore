package com.dataintuitive.luciuscore
package api.v4_1

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.SparkSession
import scala.collection.immutable.Map

import model.v4_1._
import genes._
import lenses.CombinedPerturbationLenses._

object Targets extends ApiFunctionTrait {

  case class SpecificData(
    targetQuery: String,
    limit: Int
  )

  type JobOutput = Array[Map[String, String]]

  val infoMsg = s"Lookup a list of targets based on a query, optionally with a limit on the number of results."

  val helpMsg =
    s"""
    |$infoMsg
      | Input:
        | - query: `startsWith` query on available known targets (mandatory)
        | - version: v1, v2 or t1 (optional, default is `v1`)
        | - limit: The result size is limited to this number (optional, default is 10)
     """.stripMargin


  def header(data: JobData) = "(target, #)"

  def result(data: JobData)(implicit sparkSession: SparkSession) = {

    import sparkSession.implicits._

    val CachedData(db, _, genesDb, _) = data.cachedData
    val SpecificData(targetQuery, limit) = data.specificData
    implicit val genes = genesDb

    val resultRDD =
      db.rdd.flatMap(_.trt.trt_cp)
        .distinct
        .filter(_.targets != None)
        // Some entries in the ingested data set still contain nulls...
        .filter(!_.targets.toSet.contains(null))
        .map(trt_cp => trt_cp.targets)
        .flatMap { kts =>
          kts
        }
        .filter { kt =>
          kt.startsWith(targetQuery)
        }
        .countByValue()
        .toArray
        .map { case (target, count) => Map("target" -> target, "count" -> count.toString) }

    val limitOutput = (resultRDD.size > limit)

    // Should we limit the result set?
    limitOutput match {
      case true  => resultRDD.take(limit)
      case false => resultRDD //.collect
    }

  }

}
