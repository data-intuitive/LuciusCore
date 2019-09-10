package com.dataintuitive.luciuscore

package object genes {

  type GeneType = String

  type Probesetid = String
  type Symbol = String

  type GeneDictionary = Map[Symbol, Probesetid]
  type SimpleGeneDictionary = Map[Symbol, Gene]
  type InverseGeneDictionary = Map[Probesetid,Symbol]

  type NotationType = String
  val SYMBOL = "symbol"
  val PROBESETID = "probesetid"
  val INDEX = "index"

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

    def toGeneAnnotationV2 = new StageGene(0, probesetid, "LM", Some(Set(entrezid)), Some(Set(ensemblid)), Some(Set(symbol)), Some(Set(name)), None)

  }

}
