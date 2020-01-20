package com.dataintuitive.luciuscore.genes

import org.scalatest.FlatSpec

class StageGenesTest extends FlatSpec {

  info("Test model for gene annotations")

  val gene: Gene = Gene(
    201,
    "symbol",
    List("probesets"),
    List(1,2,3))

  "methods on a gene" should "return the method field" in {
    assert(gene.index === 201)
    assert(gene.symbol === "symbol")
    assert(gene.probesetIDs === List("probesets"))
    assert(gene.probesetIndices === List(1,2,3))
  }

  val listOfGenes = Array(
    Gene(1, "symbol1"),
    Gene(2, "symbol2"),
    Gene(3, "symbol3"),
    Gene(4, "symbol4")
  )

  "A GeneDB object" should "be created" in {
    val geneDB = new GeneDB(listOfGenes)
    assert(geneDB.lookup("symbol1").get === 1)
  }

}

