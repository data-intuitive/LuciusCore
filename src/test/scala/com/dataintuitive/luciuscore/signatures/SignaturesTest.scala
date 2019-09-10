package com.dataintuitive.luciuscore.signatures

import com.dataintuitive.luciuscore.genes._
import com.dataintuitive.luciuscore.signatures._
import com.dataintuitive.luciuscore.signatures.SignedTypes._
import com.dataintuitive.luciuscore.signatures.Signatures._

// import Signatures._
// import SignedTypes._

// import signatures.SignedTypes._
// import signature.Signatures
// import com.dataintuitive.luciuscore.genes.Genes
// import com.dataintuitive.luciuscore.genes.Gene
// import Signatures._
// import SignedTypes._

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

  implicit val genes = new Genes(listOfGenes)

  "Instantiation" should "create the correct symbol objects" in {

    val ssignature1 = SymbolSignature(Array(SignedString(Sign.PLUS, "SYM1"),
                                            SignedString(Sign.MINUS, "SYM2"),
                                            SignedString(Sign.PLUS, "SYM4")))
    val ssignature2 = new SymbolSignature(Array("SYM1", "-SYM2", "SYM4"))

    ssignature1.toString should equal (ssignature2.toString)

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
}
