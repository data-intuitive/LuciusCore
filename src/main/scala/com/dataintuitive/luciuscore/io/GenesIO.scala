package com.dataintuitive.luciuscore.io

import com.dataintuitive.luciuscore.genes._
import ParseFunctions._
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

/**
  * IO convenience functions, loading from file using Spark functionality where appropriate.
  */
object GenesIO {

  /**
    * IO convenience and demonstration function. Reading gene annotations from a file and parsing it
    * to a `Genes` datastructure.
    * Remark: Even if the input does not contain all info to fill the datastructure, we need to provide a features vector of size 7!
    *
    * @param sc SparkContext
    * @param geneAnnotationsFile The location of the file
    * @param delimiter The delimiter to use when parsing the input file. Default is `tab`
    * @return Array of Gene, to be parsed to a GenesDB
    */
    def loadGenesFromFile(sc: SparkContext,
               geneAnnotationsFile: String,
               delimiter: String = "\t"): Array[Gene] = {

    val featuresToExtract = Seq(
      "probesetID", 
      "dataType", 
      "ENTREZID", 
      "ENSEMBL", 
      "SYMBOL", 
      "GENENAME", 
      "GENEFAMILY")

    val rawGenesRdd = sc.textFile(geneAnnotationsFile).map(_.split(delimiter).map(_.trim))

    val splitGenesRdd = extractFeatures(rawGenesRdd, featuresToExtract, includeHeader=false)

    val genesRaw:RDD[GeneRaw] = 
      splitGenesRdd.zipWithIndex.map{ case (x,i) => new GeneRaw(
        i.toInt + 1,              // index offset 1
        x(0).getOrElse("N/A"),
        x(1).getOrElse("N/A"),
        x(2),
        x(3),
        x(4),
        x(5),
        x(6)
      )
    }


    // Turn into RDD containing objects
    val genes: RDD[Gene] = genesRaw.map( _.toGene )

    genes.collect()

}

}
