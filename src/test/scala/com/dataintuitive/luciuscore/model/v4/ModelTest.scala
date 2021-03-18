package com.dataintuitive.luciuscore.model.v4

import org.scalatest.flatspec.AnyFlatSpec

class ModelTestv4 extends AnyFlatSpec {

  info("Test v4 Model")

  val p = Perturbation("123", Information(), Nil, TRT_EMPTY, Nil)

  "Minimal Instantiation" should "simply work" in {
    assert(p.id === "123")
    assert(p.trtType === "empty")
    assert(p.trt === TRT_EMPTY)
    assert(p.trt_cp === None)
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
      Some("m")
    )

  val trtGenericLig =
    TRT_GENERIC(
      "trt_lig",
      "pertId",
      "pertName",
      None,
      None,
      None,
      Some("2"),
      Some("ml"),
      Some("1"),
      Some("m")
    )

  val cp = Perturbation(
    id = "pid",
    info = Information(),
    profiles = Nil,
    trt = trtGenericCp,
    filters = Nil
  )
  val lig = Perturbation(
    id = "pid",
    info = Information(),
    profiles = Nil,
    trt = trtGenericLig,
    filters = Nil
  )

  "Full instantiation" should "automatically create the correct entries" in {
    assert(cp.trt !== None)
    assert(cp.trt_lig === None)
  }

  "After init, trt_generic" should "be converted and turned to None" in {
    assert(cp.trt_cp !== None)
    assert(cp.trt_generic === None)
    assert(lig.trt_lig !== None)
    assert(lig.trt_generic === None)
  }

  it should "allow for using accessor methods trt and trtSafe" in {
    assert(cp.trt === cp.trt_cp.get)
    assert(cp.trtSafe === cp.trt_cp.get)
    assert(lig.trt === lig.trt_lig.get)
    assert(lig.trtSafe === lig.trt_lig.get)
  }

}
