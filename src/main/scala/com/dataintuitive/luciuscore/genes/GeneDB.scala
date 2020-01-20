package com.dataintuitive.luciuscore.genes

case class GeneDB(val genes: Array[Gene]) {

  def lookup(symbol: Symbol) = genes.filter(_.symbol == symbol).head

}
