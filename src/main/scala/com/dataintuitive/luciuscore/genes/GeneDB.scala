package com.dataintuitive.luciuscore.genes

case class GeneDB(val genes: Array[Gene]) {

  val symbolToIndexDict = genes.map(gene => (gene.symbol -> gene.index)).toMap
  val indexToSymbolDict = symbolToIndexDict.map(_.swap)

  def lookup(symbol: Symbol):Option[Index] = symbolToIndexDict.get(symbol)
  def lookup(index: Index):Option[Symbol] = indexToSymbolDict.get(index)

}
