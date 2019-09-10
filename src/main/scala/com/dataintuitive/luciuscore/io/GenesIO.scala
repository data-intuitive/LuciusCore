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
    * @return `Genes` datastructure (in-memory array of `GeneAnnotation`)
    */
    def loadGenesFromFile(sc: SparkContext,
               geneAnnotationsFile: String,
               delimiter: String = "\t"): StageGenes = {

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

    // Turn into RDD containing objects
    val genes: RDD[StageGene] =
      splitGenesRdd.zipWithIndex.map{ case (x,i) => new StageGene(
        i.toInt + 1,              // index offset 1
        x(0).getOrElse("N/A"),
        x(1).getOrElse("N/A"),
        x(2).flatMap(convertOption(_)).flatMap(secondarySplit(_)),
        x(3).flatMap(convertOption(_)).flatMap(secondarySplit(_)),
        x(4).flatMap(convertOption(_)).flatMap(secondarySplit(_)),
        x(5).flatMap(convertOption(_)).flatMap(secondarySplit(_)),
        x(6).flatMap(convertOption(_))
      )
    }

    val asArray: Array[StageGene] = genes.collect()

    new StageGenes(asArray)
}

  /*
   * Split on the seconary field delimiter
   */
  def secondarySplit(s:String, delimiter:String = "///"):Option[Set[String]] = 
    Some(s.split(delimiter).map(_.trim).toSet)

  /*
   * The data contains fields with --- signifying no data, we convert this to None options.
   */
  def convertOption(o: String, noValue:String = "---"):Option[String] = if (o == noValue) None else Some(o)

}
