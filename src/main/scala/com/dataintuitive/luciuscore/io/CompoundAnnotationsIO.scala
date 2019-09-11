package com.dataintuitive.luciuscore.io

import com.dataintuitive.luciuscore.genes._
import org.apache.spark.SparkContext
import com.dataintuitive.luciuscore.io.ParseFunctions._
import com.dataintuitive.luciuscore.Model._
import com.dataintuitive.luciuscore.utilities.RddFunctions.joinUpdateRDD
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
        val jnjs = l.head(0)
        val inchikey = l.head(1)
        val smiles = l.head(2)
        // Targets are not necessarily filled in, filter the empty ones out
        val targets = l.flatMap(x => x(3)).toSeq.distinct
        // Return quadrupals
        (jnjs, inchikey, smiles, Some(targets))
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
  def dbKeyFunction(x:DbRow):Option[String] = x.compoundAnnotations.compound.jnjs

}
