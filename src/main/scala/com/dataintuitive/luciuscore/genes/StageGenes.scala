package com.dataintuitive.luciuscore.genes

class StageGenes(val genes: Array[StageGene]) {

  /**
   * The input contains entries with multiple symbol names, separated by `///`.
   */
  private def splitGeneAnnotationSymbols(in: String, ga: StageGene): Array[(String, StageGene)] = {
    val arrayString = in.split("///").map(_.trim)
    arrayString.flatMap(name => Map(name -> ga))
  }

}
