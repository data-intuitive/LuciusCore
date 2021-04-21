package com.dataintuitive.luciuscore

package object genes {

  type GeneType = String

  type Symbol = String

  type GeneDictionary = Map[Symbol, String]
  type SimpleGeneDictionary = Map[Symbol, Gene]
  type InverseGeneDictionary = Map[String,Symbol]

  type NotationType = String
  val SYMBOL = "symbol"
  val INDEX = "index"

  /**
    * Class for holding information about a gene, old version with only LM genes.
    */
  class GeneAnnotation(
                        val id: String,
                        val entrezid: String,
                        val ensemblid: String,
                        val symbol: Symbol,
                        val name: String) extends Serializable {

    override def toString = s"${id} (entrezid = ${entrezid}, ensemblid = ${ensemblid}, symbol = ${symbol}, name = ${name})"

    def toGeneAnnotationV2 = new Gene(0, id, "LM", Some(Set(entrezid)), Some(Set(ensemblid)), Some(Set(symbol)), Some(Set(name)), None)

  }

}
