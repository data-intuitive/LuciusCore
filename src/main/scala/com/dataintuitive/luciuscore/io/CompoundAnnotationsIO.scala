package com.dataintuitive.luciuscore
package io

import model.v3._

import genes._
import io.ParseFunctions._
import utilities.RddFunctions.joinUpdateRDD

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

/**
  * Created by toni on 12/09/16.
  */
object CompoundAnnotationsIO extends Serializable {

  def loadCompoundAnnotationsFromFileV2(sc: SparkContext, fileName:String) = {

    val compoundAnnotationsRaw = sc.textFile(fileName).map(_.split("\t"))

    val featuresToExtract = Seq("JNJ",
                                "InChIKey",
                                "Smiles",
                                "target_uniprot_gene")
    // Etract as key-value pairs
    val denormalizedCAs = extractFeaturesKV(compoundAnnotationsRaw, "JNJ", featuresToExtract)

    // Normalize the data, aggregate the targets into a Set
    denormalizedCAs
      .groupByKey
      .filter(_._1.isDefined) // Filter bogus entries
      .values
      .map(_.toSeq)
      .map { l =>
        // There is at least one entry, so take the first one
        val id = l.head(0)
        val inchikey = l.head(1)
        val smiles = l.head(2)
        // Targets are not necessarily filled in, filter the empty ones out
        val targets = l.flatMap(x => x(3)).toSeq.distinct
        // Return quadrupals
        (id, inchikey, smiles, Some(targets))
      }
  }

  def updateCompoundAnnotationsV2(normalizedCAs:org.apache.spark.rdd.RDD[(Option[String], Option[String], Option[String], Some[Seq[String]])], db:RDD[DbRow]):RDD[DbRow] = {

    joinUpdateRDD(normalizedCAs.keyBy(caKeyFunction), ca2DbRow)(db.keyBy(dbKeyFunction)).values

  }

  def ca2DbRow(ca:DbRow, update:(Option[String], Option[String],Option[String],Option[Seq[GeneType]])) = {
    ca.copy(
      compoundAnnotations=ca.compoundAnnotations.copy(compound=ca.compoundAnnotations.compound.copy(
                inchikey=update._2,
                smiles=update._3
              )).copy(knownTargets=update._4)
    )
  }

  // Key for compound ID in DB
  def caKeyFunction(x:(Option[String], Option[String],Option[String],Option[Seq[GeneType]])):Option[String] = x._1
  // Key for compound ID in new data to be joined
  def dbKeyFunction(x:DbRow):Option[String] = x.compoundAnnotations.compound.id

}
