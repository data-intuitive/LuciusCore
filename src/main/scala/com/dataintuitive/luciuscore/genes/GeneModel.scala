package com.dataintuitive.luciuscore.genes

/**
  * A model for a gene annotation and a collection of genes.
  */
object GeneModel extends Serializable {

  // A GeneVector is an ordered list of Genes (aka signature)
  type Gene = String
  type GeneVector = Array[Gene]

  type Probesetid = String
  type Symbol = String

  type GeneDictionary = Map[Symbol, Probesetid]
  type SimpleGeneDictionary = Map[Symbol, GeneAnnotationV2]
  type InverseGeneDictionary = Map[Probesetid,Symbol]

  type NotationType = String
  val SYMBOL = "symbol"
  val PROBESETID = "probesetid"
  val INDEX = "index"

  /**
    * Class for holding information about a gene.
    */
  class GeneAnnotationV2(
                        val probesetid: Probesetid,
                        val dataType: String,
                        val entrezid: Option[String],
                        val ensemblid: Option[String],
                        val symbol: Option[Symbol],
                        val name: Option[String],
                        val geneFamily: Option[String]) extends Serializable {

    override def toString = s"${probesetid} (entrezid = ${entrezid}, dataType = ${dataType}, ensemblid = ${ensemblid}, symbol = ${symbol}, name = ${name}, family = ${geneFamily})"

  }

  /**
    * Class for holding information about a gene, old version with only LM genes.
    */
  class GeneAnnotation(
                        val probesetid: Probesetid,
                        val entrezid: String,
                        val ensemblid: String,
                        val symbol: Symbol,
                        val name: String) extends Serializable {

    override def toString = s"${probesetid} (entrezid = ${entrezid}, ensemblid = ${ensemblid}, symbol = ${symbol}, name = ${name})"
    
    def toGeneAnnotationV2 = new GeneAnnotationV2(probesetid, "LM", Some(entrezid), Some(ensemblid), Some(symbol), Some(name), None)

  }

  class Genes(val genes: Array[GeneAnnotationV2]) {

    /**
      * The input contains entries with multiple symbol names, separated by `///`.
      */
    private def splitGeneAnnotationSymbols(in: String, ga: GeneAnnotationV2): Array[(String, GeneAnnotationV2)] = {
      val arrayString = in.split("///").map(_.trim)
      return arrayString.flatMap(name => Map(name -> ga))
    }

    /**
      * Create a dictionary
      * 
      * Filter out entries that do not contain a SYMBOL representation of the gene.
      * When multiple SYMBOLS are present, duplicate the entry.
      */
    def createSymbolDictionary: SimpleGeneDictionary = {
      genes
        .filter(ga => ga.symbol != None)
        .flatMap(ga => splitGeneAnnotationSymbols(ga.symbol.get, ga))
        .toMap
    }

    /**
      * Dictionary to translate symbols to probsetids
      */
    val symbol2ProbesetidDict = createSymbolDictionary.map{ case (s,ga) => (s, ga.probesetid)}.toMap

    /**
      * Dictionary to translate indices to probesetids.
      *
      * Remark: offset 1 is important for consistency when translating between dense and sparse format
      * 
      * Note: This is a simple copy from the first iteration of this class. Please refer to the indexed versions.
      */
    val index2ProbesetidDict: Map[Int, Probesetid] =
        genes
          .map(_.probesetid)
          .zipWithIndex
          .map(tuple => (tuple._1, tuple._2 + 1))
          .map(_.swap)
          .toMap

    /**
      * A vector containing the probesetids representing the genes.
      */
    val probesetidVector = genes.map(_.probesetid)

  }

}
