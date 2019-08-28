package com.dataintuitive.luciuscore

package object genes {

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

}