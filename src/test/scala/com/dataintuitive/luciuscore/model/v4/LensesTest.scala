package com.dataintuitive.luciuscore.model.v4

import Model._
import com.dataintuitive.luciuscore.model.v4.lenses.PerturbationLenses

import org.scalatest.flatspec.AnyFlatSpec

class LensesTest extends AnyFlatSpec {

  info("Test v4 Model")

  val p = Perturbation("123", trt = TRT_CP(name = "test123", concentration = "conc1"))

  "Minimal Instantiation" should "simply work" in {
    assert(p.id === "123")
  }

  it should "work with lenses" in {
    val thisLens = PerturbationLenses.idLens
    assert(thisLens.get(p) === "123")
    assert(thisLens.get(thisLens.set(p, "456")) === "456")
  }

}
