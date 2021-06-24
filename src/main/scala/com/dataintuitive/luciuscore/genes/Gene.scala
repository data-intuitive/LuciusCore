package com.dataintuitive.luciuscore.genes

/**
  * Class for holding information probsets and genes.
  *
  * Variables modelled with a `Set` may contain multiple entries.
  */
class Gene(
                val index: Int,
                val id: String,
                val dataType: String,
                val entrezid: Option[Set[String]],
                val ensemblid: Option[Set[String]],
                val symbol: Option[Set[Symbol]],
                val name: Option[Set[String]],
                val geneFamily: Option[String]) extends Serializable {

  override def toString = s"${id} (index = ${index}, entrezid = ${entrezid}, dataType = ${dataType}, ensemblid = ${ensemblid}, symbol = ${symbol}, name = ${name}, family = ${geneFamily})"

}
