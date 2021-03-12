package com.dataintuitive.luciuscore.model.v4

import org.scalatest.flatspec.AnyFlatSpec

class ModelTestv4 extends AnyFlatSpec {

  info("Test v4 Model")

  val p = Perturbation("123")

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
    "pid",
    Information(),
    Nil,
    trtGenericCp,
    Nil
  )
  val lig = Perturbation(
    "pid",
    Information(),
    Nil,
    trtGenericLig,
    Nil
  )

  "Full instantiation" should "automatically create the correct entries" in {
    assert(cp.trt === trtGenericCp)
    assert(cp.trt_cp === ModelFunctions.convertToSpecific(trtGenericCp))
  }

}
