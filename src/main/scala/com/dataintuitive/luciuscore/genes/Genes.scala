package com.dataintuitive.luciuscore.genes

case class GenesDB(val genes: Array[Gene]) extends Serializable {

  /**
   * The input contains entries with multiple symbol names, separated by `///`.
   */
  private def splitGeneAnnotationSymbols(in: String, ga: Gene): Array[(String, Gene)] = {
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
  def createIndexDictionary: Map[Int, Gene] = {
    genes
      .map(ga => (ga.index, ga))
      .toMap
  }

  /**
   * Dictionary to translate symbols to probsetids
   *
   * Please note the relation id <-> symbol is n-m
   */
  val symbol2idDict =
    createSymbolDictionary
      .map{ case (s, ga) => (s, ga.id) }

  /**
   * Dictionary to translate indices to ids.
   *
   * This mapping is 1-1
   */
  val index2idDict: Map[Int, String] =
    createIndexDictionary
      .map{ case (i, ga) => (i, ga.id) }

  /**
   * Dictionary to translate indices to ids.
   *
   * This mapping is 1-1
   */
  val id2index: Map[String, Int] =
    for ((k,v) <- index2idDict) yield (v, k)

  /**
   * A vector containing the ids representing the genes.
   */
  val idVector = genes.map(_.id)

}
