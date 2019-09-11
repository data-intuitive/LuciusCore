package com.dataintuitive.luciuscore.genes

/**
 * Class to handle gene/probeset information raw from file
 * and process it further before converting to Genes/Gene types.
 */
class StageGenes(val genes: Array[StageGene]) {

  /**
   * The input contains entries with multiple symbol names, separated by `///`.
   */
  private def splitGeneAnnotationSymbols(in: String, ga: StageGene): Array[(String, StageGene)] = {
    val arrayString = in.split("///").map(_.trim)
    arrayString.flatMap(name => Map(name -> ga))
  }

  /**
   * Temporary way to convert stageGenes to Genes.
   * WIP/TODO
   */
  def toGenes:Genes = new Genes(genes.map(stageGene =>
      new Gene(
                stageGene.index,
                stageGene.probesetid,
                stageGene.dataType,
                stageGene.entrezid,
                stageGene.ensemblid,
                stageGene.symbol,
                stageGene.name,
                stageGene.geneFamily
    )))

}
