package com.dataintuitive.luciuscore.genes

class Genes(val genes: Array[GeneAnnotationV2]) {

  /**
   * The input contains entries with multiple symbol names, separated by `///`.
   */
  private def splitGeneAnnotationSymbols(in: String, ga: GeneAnnotationV2): Array[(String, GeneAnnotationV2)] = {
    val arrayString = in.split("///").map(_.trim)
    arrayString.flatMap(name => Map(name -> ga))
  }

  /**
   * Create a dictionary symbol -> record
   *
   * Filter out entries that do not contain a SYMBOL representation of the gene.
   * When multiple SYMBOLS are present, duplicate the entry.
   */
  def createSymbolDictionary: SimpleGeneDictionary = {
    genes
      .filter(ga => ga.symbol != None)
      .map(ga => ga.symbol.get.map( s => (s, ga)))
      .flatMap(x => x)
      .toMap
  }

  /**
   * Create a dictionary index -> record
   */
  def createIndexDictionary: Map[Int, GeneAnnotationV2] = {
    genes
      .map(ga => (ga.index, ga))
      .toMap
  }

  /**
   * Dictionary to translate symbols to probsetids
   *
   * Please note the relation probesetid <-> symbol is n-m
   */
  val symbol2ProbesetidDict = 
    createSymbolDictionary
      .map{ case (s, ga) => (s, ga.probesetid) }

  /**
   * Dictionary to translate indices to probesetids.
   *
   * This mapping is 1-1
   */
  val index2ProbesetidDict: Map[Int, Probesetid] =
    createIndexDictionary
      .map{ case (i, ga) => (i, ga.probesetid) }

  /**
   * A vector containing the probesetids representing the genes.
   */
  val probesetidVector = genes.map(_.probesetid)

}
