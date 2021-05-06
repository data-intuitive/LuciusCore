package com.dataintuitive.luciuscore.model.v4

import Treatment._

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should._

class ModelTestv4 extends AnyFlatSpec with Matchers {

  info("Test v4 Model")

  val p = Perturbation("123", Information(), Profiles(), TRT_EMPTY, Nil)

  "Minimal Instantiation" should "simply work" in {
    assert(p.id === "123")
    assert(p.trtType === "empty")
  }

  it should "allow simple checks for consistency and empty" in {
    assert(p.trt.isEmpty)
    assert(!p.trt.isConsistent)
  }

  val trtGenericCp =
    TRT_GENERIC(
      "trt_cp",
      "pertId",
      "pertName",
      Some("inchikey"),
      Some("smiles"),
      Some("pubchem"),
      Some("1"),
      Some("ml"),
      Some("4"),
      Some("m"),
      Some(List("T1", "T2"))
    )

  val perturbation = Perturbation("456", Information(), Profiles(), trtGenericCp, Nil)

  "Full instantiation" should "automatically create the correct entries" in {

    val trtCp = TRT_CP(
        name = "pertName",
        id = "pertId",
        dose = "1",
        doseUnit = "ml",
        time = "4",
        timeUnit = "m",
        inchikey = Some("inchikey"),
        smiles = Some("smiles"),
        pubchemId = Some("pubchem"),
        targets = List("T1", "T2")
      )

    assert(perturbation.trt.trt === trtCp)

  }

  it should "allow easy access to attributes" in {
    assert(perturbation.trt.get.id === "pertId")
    assert(perturbation.id === "456")
    assert(perturbation.trtType === "trt_cp")
    assert(perturbation.trt.get.trtType === "trt_cp")
  }

  it should "allow automatic conversion to trt_<specific>" in {
    assert(perturbation.trt.isConsistent)
    perturbation.trt.get shouldBe a [TRT_CP]
    perturbation.trt.isSpecific shouldBe true
    assert(perturbation.trt.trt_generic === None)
  }

}
