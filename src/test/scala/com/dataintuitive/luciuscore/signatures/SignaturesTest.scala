package com.dataintuitive.luciuscore.signatures

import com.dataintuitive.luciuscore.genes._

import org.scalatest.{FlatSpec, Matchers}

class SignaturesTest extends FlatSpec with Matchers {

  val listOfGenes = Array(
    new Gene(1, "psid1", "AF", None, None, Some(Set("SYM1")), None, None),
    new Gene(2, "psid2", "AF", None, None, Some(Set("SYM2")), None, None),
    new Gene(3, "psid3", "AF", None, None, Some(Set("SYM3")), None, None),
    new Gene(4, "psid4", "AF", None, None, Some(Set("SYM4")), None, None),
    new Gene(5, "psid5", "AF", None, None, Some(Set("SYM5")), None, None),
    new Gene(6, "psid6", "AF", None, None, Some(Set("SYM6")), None, None)
  )

  implicit val genes = new GenesDB(listOfGenes)

  "Instantiation" should "create the correct symbol objects" in {

    val ssignature1 = SymbolSignature(Array(SignedString(Sign.PLUS, "SYM1"),
                                            SignedString(Sign.MINUS, "SYM2"),
                                            SignedString(Sign.PLUS, "SYM4")))
    val ssignature2 = new SymbolSignature(Array("SYM1", "-SYM2", "SYM4"))

    ssignature1.toString should equal (ssignature2.toString)

  }

  it should "be possible to convert to Array[String] using toArray" in {
    
    val a = Array("SYM1", "-SYM2", "SYM4")
    val ssignature = new SymbolSignature(a)
    ssignature.toArray should equal (a)

  }

  it should "create the correct probesetid objects" in {

    val psignature1 = ProbesetidSignature(Array(SignedString(Sign.PLUS, "psid1"),
                                            SignedString(Sign.MINUS, "psid2"),
                                            SignedString(Sign.PLUS, "psid4")))
    val psignature2 = new ProbesetidSignature(Array("psid1", "-psid2", "psid4"))

    psignature1.toString should equal (psignature2.toString)

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
    val psignature2 = new ProbesetidSignature(Array("psid1", "-psid2", "psid4"))
    val isignature2 = new IndexSignature(Array(1, -2, 4))

    ssignature2.toProbesetidSignature.toString should equal (psignature2.toString)
    psignature2.toIndexSignature.toString should equal (isignature2.toString)
    isignature2.toSymbolSignature.toString should equal (ssignature2.toString)

  }

  "Translation" should "return OOPS for unknown entries" in {
    val invalidSSignature = new SymbolSignature(Array("-SYM1", "OOPS"))
    val invalidPSignature = new ProbesetidSignature(Array("-psid1", "NA"))

    invalidPSignature.toSymbolSignature.toString should equal (invalidSSignature.toString)

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
