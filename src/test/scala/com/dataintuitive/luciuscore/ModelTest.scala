package com.dataintuitive.luciuscore.model.v3

import Model._

import org.scalatest.flatspec.AnyFlatSpec

class ModelTest extends AnyFlatSpec {

  info("Test Experimental Model")

  val minSample = Sample("MinSample")
  val minCompound = Compound("MinCompound")
  val minSampleAnnotations = SampleAnnotations(minSample)
  val minCompoundAnnotations = CompoundAnnotations(minCompound)

  "Minimal Instantiation" should "simply work" in {
    assert(minSample.getPwid === "MinSample")
    assert(minCompound.getJnj === "MinCompound")
    assert(minSample.year === None)
    assert(minSample.getYear === "Sample year not available")
    assert(minCompound.smiles === None)
    assert(minSampleAnnotations.sample == minSample)
    assert(minCompoundAnnotations.compound == minCompound)
  }

  val sample1 = Sample(Some("sample1"), year=Some("2010"), concentration=Some("0.1"))
  val sample2 = Sample(Some("sample2"), year=Some("2012"), concentration=Some("0.2"))
  val compound1 = Compound(Some("compound1"), smiles=Some("43jbdhadfn3oure"), inchikey=Some("inchikey1"))
  val compound2 = Compound(Some("compound2"), smiles=Some("bdslkhjfadoi43d"), inchikey=Some("inchikey2"))

}
