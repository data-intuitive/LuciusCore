package com.dataintuitive.luciuscore.signatures

import org.scalatest.{FlatSpec, Matchers}

class SignedTypesTest extends FlatSpec with Matchers {

  "SignedString and companion object" should "construct the correct representation" in {

    val ss1 = SignedString(Sign.PLUS, "aString")
    val ss2 = SignedString("aString")
    val ss3 = SignedString(Sign.MINUS, "anotherString")
    val ss4 = SignedString("-anotherString")

    ss1 should equal (ss2)
    ss3 should equal (ss4)

  }

  it should "allow for easy access and transformation" in {

    val ss2 = SignedString("aString")
    val ss4 = SignedString("-anotherString")

    ss2.abs should equal ("aString")
    ss2.sign should equal (Sign.PLUS)
    ss2.signString should equal ("")
    ss4.abs should equal ("anotherString")
    ss4.sign should equal (Sign.MINUS)
    ss4.signString should equal ("-")

  }

  "SignedInt and companion object" should "construct the correct representation" in {

    val ss1 = SignedInt(Sign.PLUS, 3)
    val ss2 = SignedInt(3)
    val ss3 = SignedInt(Sign.MINUS, 3)
    val ss4 = SignedInt(-3)

    ss1 should equal (ss2)
    ss3 should equal (ss4)

  }

  it should "allow for easy access and transformation" in {

    val ss2 = SignedInt(3)
    val ss4 = SignedInt(-3)

    ss2.abs should equal (3)
    ss2.sign should equal (Sign.PLUS)
    ss2.signInt should equal (1)
    ss2.signString should equal ("")
    ss4.abs should equal (3)
    ss4.sign should equal (Sign.MINUS)
    ss4.signInt should equal (-1)
    ss4.signString should equal ("-")

  }

  // val validSymbolSignatureCreated = SymbolSignature(Array("symbol2", "-symbol1"))
  // val validProbesetidSignatureCreated = ProbesetidSignature(Array("psid2", "-psid1"))

  // "Signature objects" should "be created correctly" in {
  //   validSymbolSignatureCreated.toString should equal ("Signature of type symbol: [symbol2,-symbol1]")
  // }

  // "Companion object for Probeset" should "create correct object" in {
  //   validProbesetidSignatureCreated.toString should equal ("Signature of type probesetid: [psid2,-psid1]")
  // }

  // val listOfGenes = Array(
  //   new StageGene(0, "psid1", "AF", None, None, Some(Set("SYM1")), None, None),
  //   new StageGene(1, "psid2", "AF", None, None, Some(Set("SYM2")), None, None),
  //   new StageGene(2, "psid3", "AF", None, None, Some(Set("SYM3")), None, None),
  //   new StageGene(3, "psid4", "AF", None, None, Some(Set("SYM4")), None, None),
  //   new StageGene(4, "psid5", "AF", None, None, Some(Set("SYM5")), None, None),
  //   new StageGene(5, "psid6", "AF", None, None, Some(Set("SYM6")), None, None)
  // )

  // // Translation is done using a dictionary
  // val dict:GeneDictionary = Map("symbol1" -> "psid1", "symbol2" -> "psid2")

  // val validSymbolSignature = SymbolSignature(Array("symbol2", "-symbol1"))
  // val validProbesetidSignature = ProbesetidSignature(Array("psid2", "-psid1"))
  // val invalidSymbolSignature = SymbolSignature(Array("-symbol1", "symbol3"))
  // val invalidProbesetidSignature = ProbesetidSignature(Array("-psid1", "psid3"))

  // "The notation for a symbol signature" should "be correct" in {
  //   assert(validSymbolSignature.notation === "symbol")
  //   assert(validProbesetidSignature.notation === "probesetid")
  // }

  // "A valid SymbolSignature" should "translate into a valid ProbesetidSignature" in {
  //   assert(validSymbolSignature.translate2Probesetid(dict).signature === validProbesetidSignature.signature)
  // }

  // "A valid ProbesetidSignature" should "translate into a valid SymbolSignature" in {
  //   assert(validProbesetidSignature.translate2Symbol(dict).signature === validSymbolSignature.signature)
  // }

  // "Two consecutive translations" should "return identity" in {
  //   assert(validProbesetidSignature.translate2Symbol(dict).translate2Probesetid(dict).signature === validProbesetidSignature.signature)
  //   assert(validSymbolSignature.translate2Probesetid(dict).translate2Symbol(dict).signature === validSymbolSignature.signature)
  // }

  // "Safe translation" should "return options instead of Strings" in {
  //   assert(invalidProbesetidSignature.safeTranslate2Symbol(dict) === Array(Option("-symbol1"), None))
  //   assert(invalidSymbolSignature.safeTranslate2Probesetid(dict) === Array(Option("-psid1"), None))
  // }

  // "Translation" should "return OOPS for unknown entries" in {
  //   assert(invalidProbesetidSignature.translate2Symbol(dict).signature === Array("-symbol1", "OOPS"))
  //   assert(invalidSymbolSignature.translate2Probesetid(dict).signature === Array("-psid1", "OOPS"))
  // }

  // info("Test GeneSignature model, throw index-based signature in the mix")

  // // Translation is done using a dictionary
  // val indexDict = Map(1 -> "psid1", 2 -> "psid2")

  // val indexSignature = new IndexSignature(Array("2", "-1"))

  // "The notation for a index signature" should "be correct" in {
  //   assert(indexSignature.notation === "index")
  // }

  // "A probesetidSignature" should "translate into an index signature" in {
  //   assert(validProbesetidSignature.translate2Index(indexDict).toString === indexSignature.toString)
  // }

  // "A symbol signature" should "first be translated, then converted into an index signature" in {
  //   assert(validSymbolSignature.translate2Probesetid(dict).translate2Index(indexDict).toString === indexSignature.toString)
  // }

  // "An index signature" should "translate into the correct probesetid signature and symbol signature" in {
  //   assert(indexSignature.translate2Probesetid(indexDict).toString === validProbesetidSignature.toString)
  //   assert(indexSignature.translate2Probesetid(indexDict).translate2Symbol(dict).toString === validSymbolSignature.toString)
  // }

  // "An index signature" should "convert to signed int sparse signature" in {
  //   assert(indexSignature.asSignedInt === Array(2,-1))
  // }

  // info("Test the companion object")

  // "The companion object" should "create a signature object of type symbol" in {
  //   assert(Signature(Array("symbol2", "-symbol1")).signature === validSymbolSignature.signature)
  //   assert(Signature(Array("symbol2", "-symbol1")).notation === "symbol")
  // }

  // it should "create a signature of provided type" in {
  //   assert(Signature(Array("Symbol"), notation = "symbol").notation === "symbol")
  //   assert(Signature(Array("Probesetid"), notation = "probesetid").notation === "probesetid")
  //   assert(Signature(Array("1"), notation = "index").notation === "index")
  //   // Default for undefined notation is symbol
  //   assert(Signature(Array("NA"), notation = "undefined").notation === "symbol")
  // }
}
