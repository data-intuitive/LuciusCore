package com.dataintuitive.luciuscore.io

import com.dataintuitive.luciuscore.genes._
import ParseFunctions._
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

/**
  * IO convenience functions, loading from file using Spark functionality where appropriate.
  */
object ProbesetsIO {

  /**
    * IO convenience and demonstration function. Reading gene annotations from a file and parsing it
    * to a `Probesets` datastructure.
    * Remark: Even if the input does not contain all info to fill the datastructure, we need to provide a features vector of size 7!
    *
    * @param sc SparkContext
    * @param geneAnnotationsFile The location of the file
    * @param delimiter The delimiter to use when parsing the input file. Default is `tab`
    * @return Array of Gene, to be parsed to a ProbesetsDB
    */
  def loadProbesetsFromFile(
           sc: SparkContext,
           path: String,
           delimiter: String = "\t"): Array[Probeset] = {

    val featuresToExtract = Seq(
      "index",
      "probesetID",
      "dataType",
      "entrezID",
      "ensemblID",
      "swissprotID",
      "symbol",
      "name",
      "unigeneClusterType",
      "chromosomalLocation",
      "geneOntologyBiologicalProcess")

    val rawProbesetsRdd = loadTSV(sc, path, delimiter)

    val rawProbesets = extractFeatures(rawProbesetsRdd, featuresToExtract, includeHeader=false)

    val probesets:Array[Probeset] =
      rawProbesets.collect.map( row =>
        Probeset(
            index        = row(0).map(_.toInt).getOrElse(-1),
            probesetID   = row(1).getOrElse("NA"),
            dataType     = row(2).getOrElse("NA"),
            entrezID     = row(3).map(_.split("//")).getOrElse(Array()).toList,
            ensemblID    = row(4).map(_.split("//")).getOrElse(Array()).toList,
            symbol       = row(5).map(_.split("//")).getOrElse(Array()).toList,
            name         = row(6).getOrElse(""),
            swissprotID         = row(7).map(_.split("//")).getOrElse(Array()).toList,
            unigeneClusterType  = row(8).getOrElse(""),
            chromosomalLocation = row(9).getOrElse(""),
            geneOntologyBiologicalProcess = row(10).getOrElse("")
        )
      )

    probesets

  }

}

