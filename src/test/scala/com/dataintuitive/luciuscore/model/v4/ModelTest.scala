package com.dataintuitive.luciuscore.model.v4

import org.scalatest.flatspec.AnyFlatSpec

class ModelTestv4 extends AnyFlatSpec {

  info("Test v4 Model")

  val p = Perturbation("123", trt = TRT_CP(name = "test123", concentration = "conc1"))

  "Minimal Instantiation" should "simply work" in {
    assert(p.id === "123")
  }

}
