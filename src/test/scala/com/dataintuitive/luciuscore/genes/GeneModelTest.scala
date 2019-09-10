package com.dataintuitive.luciuscore.genes

import org.scalatest.FlatSpec

class StageGenesTest extends FlatSpec {

  info("Test model for gene annotations")

  val gene: GeneAnnotation = new GeneAnnotation("probesetidString",
    "entrezidString",
    "ensemblidString",
    "symbolString",
    "nameString")

  "methods on a gene" should "return the method field" in {
    assert(gene.name === "nameString")
    assert(gene.symbol === "symbolString")
    assert(gene.ensemblid === "ensemblidString")
    assert(gene.entrezid === "entrezidString")
    assert(gene.probesetid === "probesetidString")
  }

  val listOfGenes = Array(
    new StageGene(0, "psid1", "AF", None, None, Some(Set("SYM1")), None, None),
    new StageGene(1, "psid2", "AF", None, None, Some(Set("SYM2")), None, None),
    new StageGene(2, "psid3", "AF", None, None, Some(Set("SYM3")), None, None),
    new StageGene(3, "psid4", "AF", None, None, Some(Set("SYM4")), None, None),
    new StageGene(4, "psid5", "AF", None, None, Some(Set("SYM5")), None, None),
    new StageGene(5, "psid6", "AF", None, None, Some(Set("SYM6")), None, None)
  )

  "A Genes object" should "be created" in {
    val genes = new StageGenes(listOfGenes)
    assert(genes.genes(0).index === 0)
  }

}

