package com.dataintuitive.luciuscore
package api

import model.v4._
import genes._

import org.apache.spark
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.SparkSession

import genes._

object Filters extends ApiFunctionTrait {

  case class SpecificData()

  type JobOutput = Map[String, Any]

  val infoMsg = "Filters available in the data (excluding orig_ filters)"

  val helpMsg =
    "Return the filters and values used in the dataset.\nNo input is required. Pass null for parameters in Scala"

  def header(data: JobData) = Map("key" -> "value").toString

  def result(data: JobData)(implicit sparkSession: SparkSession) = {

    import sparkSession.implicits._

    val CachedData(db, _, genesDb) = data.cachedData
    implicit val genes = genesDb

    val filterKeys = db.flatMap(_.filters.map(_.key)).distinct.collect
    val filterKeysWithoutOrig = filterKeys.filter( x => ! (x contains "orig_") )

    val values = filterKeysWithoutOrig
      .map(key => ( key, db.flatMap(_.filters.filter(_.key == key).map(_.value)).distinct.collect) )

    values.toMap
  }

}
