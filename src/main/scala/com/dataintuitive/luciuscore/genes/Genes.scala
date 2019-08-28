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
   * Create a dictionary
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
