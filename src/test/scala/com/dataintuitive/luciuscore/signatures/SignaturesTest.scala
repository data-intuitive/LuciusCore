package com.dataintuitive.luciuscore.signatures

import com.dataintuitive.luciuscore.genes._

import org.scalatest.{FlatSpec, Matchers}

class SignaturesTest extends FlatSpec with Matchers {

  val listOfGenes = Array(
    new Gene(1, "SYM1"),
    new Gene(2, "SYM2"),
    new Gene(3, "SYM3"),
    new Gene(4, "SYM4"),
    new Gene(5, "SYM5")
  )

  implicit val genes = new GeneDB(listOfGenes)

  "Instantiation" should "create the correct symbol objects" in {

    val ssignature1 = SymbolSignature(Array(SignedString(Sign.PLUS, "SYM1"),
                                            SignedString(Sign.MINUS, "SYM2"),
                                            SignedString(Sign.PLUS, "SYM4")))
    val ssignature2 = new SymbolSignature(Array("SYM1", "-SYM2", "SYM4"))

    ssignature1.toString should equal (ssignature2.toString)

  }

  it should "create the correct index objects" in {

    val isignature1 = IndexSignature(Array(SignedInt(Sign.PLUS, 1),
                                            SignedInt(Sign.MINUS, 2),
                                            SignedInt(Sign.PLUS, 4)))
    val isignature2 = new IndexSignature(Array(1, -2, 4))

    isignature1.toString should equal (isignature2.toString)

  }

  "Given a Genes database" should "enable conversion between signatures" in {

    val ssignature2 = new SymbolSignature(Array("SYM1", "-SYM2", "SYM4"))
    val isignature2 = new IndexSignature(Array(1, -2, 4))

    ssignature2.toIndexSignature.signature should equal (isignature2.signature)
    isignature2.toSymbolSignature.signature should equal (ssignature2.signature)

  }

  "Translation" should "return OOPS or index 0 for unknown entries" in {
    val wrongssignature = new SymbolSignature(Array("SYM1", "wrong"))
    val wrongisignature = new IndexSignature(Array(1, 50000))

    wrongssignature.toIndexSignature.signature(1) should equal(SignedInt(0))
    wrongisignature.toSymbolSignature.signature(1).toString should equal("OOPS")

  }

  val indexSignature = new IndexSignature(Array(1, -3))
  val rankVector = indexSignature.toOrderedRankVector(3) // Array(2.0, 0.0, -1.0)
  val rankVector2 = Array(-1.0, 0.0, 2.0)

  "An IndexSignature" should "convert to a dense vector of given length" in {
    assert(rankVector === Array(2.0, 0.0, -1.0))
  }

  it should "convert to a dense vector of length smaller than the signature" in {
    assert(indexSignature.toOrderedRankVector(1) === Array(2.0))
  }

  it should "convert to a dense vector of given length for unordered as well" in {
    assert(indexSignature.toUnorderedRankVector(3) === Array(1.0, 0.0, -1.0))
  }

}
