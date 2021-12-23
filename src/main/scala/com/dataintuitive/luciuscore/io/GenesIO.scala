package com.dataintuitive.luciuscore
package io

import genes._
import ParseFunctions._
import IoFunctions._

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

/**
  * IO convenience functions, loading from file using Spark functionality where appropriate.
  */
object GenesIO {

  val defaultDict = Map(
    "probesetID" -> "id",
    "dataType" -> "dataType",
    "ENTREZID" -> "entrezid",
    "ENSEMBL" -> "ensemblid",
    "SYMBOL" -> "symbol",
    "GENENAME" -> "name",
    "GENEFAMILY" -> "geneFamily"
    )

  /**
    * IO convenience and demonstration function. Reading gene annotations from a file and parsing it
    * to a `Gene` datastructure.
    *
    * @param sc SparkContext
    * @param dict A dictionary file to convert effective gene input file contents to the model
    * @param geneAnnotationsFile The location of the file
    * @param delimiter The delimiter to use when parsing the input file. Default is `tab`
    * @return Array of Gene, to be parsed to a GenesDB
    */
  def loadGenesFromFile(
             sc: SparkContext,
             geneAnnotationsFile: String,
             delimiter: String = "\t",
             dict: Map[String, String] = defaultDict
            ): Array[Gene] = {

      val inverseMap = dict.map(_.swap).withDefault(_ => "N/A")

      val rawGenesRdd = sc.textFile(geneAnnotationsFile).map(_.split(delimiter).map(_.trim).map(removeQuotes _))

      val splitGenesRdd = extractFeaturesMap(rawGenesRdd, dict.keys.toSeq, includeHeader=false)

     val genesRaw:RDD[GeneRaw] =
        splitGenesRdd.zipWithIndex.map{ case (x,i) => new GeneRaw(
          i.toInt + 1,              // index offset 1
          if (inverseMap.get("id").isDefined) x(inverseMap("id")).getOrElse("N/A").toString else "N/A",
          if (inverseMap.get("dataType").isDefined) {
            if (inverseMap.get("dataType2").isDefined) {
              val dataType1 = x(inverseMap("dataType")).getOrElse("N/A").toString
              val dataType2 = x(inverseMap("dataType2")).getOrElse("N/A").toString
              dataType1 + "-" + dataType2
            }
            else
              x(inverseMap("dataType")).getOrElse("N/A").toString
          } else "N/A",
          if (inverseMap.get("entrezid").isDefined) x(inverseMap("entrezid")) else None,
          if (inverseMap.get("ensemblid").isDefined) x(inverseMap("ensemblid")) else None,
          if (inverseMap.get("symbol").isDefined) x(inverseMap("symbol")) else None,
          if (inverseMap.get("name").isDefined) x(inverseMap("name")) else None,
          if (inverseMap.get("geneFamily").isDefined) x(inverseMap("geneFamily")) else None
        )
      }


      // Turn into RDD containing objects
      val genes: RDD[Gene] = genesRaw.map( _.toGene )

      genes.collect()

    }
}
