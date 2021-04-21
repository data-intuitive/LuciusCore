package com.dataintuitive.luciuscore.genes

/**
  * Class for holding information probsets and genes.
  *
  * Variables modelled with a `Set` may contain multiple entries.
  */
class GeneRaw(
                val index: Int,
                val id: String,
                val dataType: String,
                val entrezid: Option[String],
                val ensemblid: Option[String],
                val symbol: Option[Symbol],
                val name: Option[String],
                val geneFamily: Option[String]) extends Serializable {

  override def toString = s"${id} (index = ${index}, entrezid = ${entrezid}, dataType = ${dataType}, ensemblid = ${ensemblid}, symbol = ${symbol}, name = ${name}, family = ${geneFamily})"

  def toGene = new Gene(
        index,
        id,
        dataType,
        entrezid.flatMap(convertOption(_)).flatMap(secondarySplit(_)),
        ensemblid.flatMap(convertOption(_)).flatMap(secondarySplit(_)),
        symbol.flatMap(convertOption(_)).flatMap(secondarySplit(_)),
        name.flatMap(convertOption(_)).flatMap(secondarySplit(_)),
        geneFamily.flatMap(convertOption(_))
      )


  /*
   * Split on the seconary field delimiter
   */
  def secondarySplit(s:String, delimiter:String = "///"):Option[Set[String]] = 
    Some(s.split(delimiter).map(_.trim).toSet)

  /*
   * The data contains fields with --- signifying no data, we convert this to None options.
   */
  def convertOption(o: String, noValue:String = "---"):Option[String] = if (o == noValue) None else Some(o)
}

