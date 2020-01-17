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
   * Class for holding probeset information, directly fetched from an enriched input file.
   */
  case class Probeset (
          index: Int,
          probesetID: String,
          dataType: String,
          entrezID: List[String],
          ensemblID: List[String],
          swissprotID: List[String],
          symbol: List[String],
          name: String,
          unigeneClusterType: String,
          chromosomalLocation: String,
          geneOntologyBiologicalProcess: String
  ) extends Serializable {

    def toTSV:String =
        index + "\t" +
        probesetID + "\t" +
        dataType + "\t" +
        entrezID.mkString(" // ") + "\t" +
        ensemblID.mkString("//") + "\t" +
        swissprotID.mkString("//") + "\t" +
        symbol.mkString("//") + "\t" +
        name + "\t" +
        unigeneClusterType + "\t" +
        chromosomalLocation + "\t" +
        geneOntologyBiologicalProcess

  }


  /**
   * Class for containing symbol-based gene information with a reference to the original probesets.
   */
  case class Gene (
      val index: Int,
      val symbol: String,
      val probesetIDs: List[Probesetid] = List(),
      val probesetIndices: List[Int] = List()
  ) extends Serializable {

    override def toString =
      s"${symbol} " +
      s"(index = ${index}," +
      s" probesetIDs = ${probesetIDs.mkString(", ")}," +
      s" ProbesetIndices = ${probesetIndices.mkString(", ")})"

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

    def toGeneAnnotationV2 = new Gene(0, probesetid, "LM", Some(Set(entrezid)), Some(Set(ensemblid)), Some(Set(symbol)), Some(Set(name)), None)

  }

}
