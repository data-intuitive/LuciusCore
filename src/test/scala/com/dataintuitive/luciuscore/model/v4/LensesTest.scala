package com.dataintuitive.luciuscore.model.v4

import model.v4.lenses._

import org.scalatest.flatspec.AnyFlatSpec

class LensesTest extends AnyFlatSpec {

  info("Test v4 Model")

  val p = Perturbation("123")

  "Minimal Instantiation" should "simply work" in {
    assert(p.id === "123")
  }

  it should "work with lenses" in {
    val thisLens = PerturbationLenses.idLens
    assert(thisLens.get(p) === "123")
    assert(thisLens.get(thisLens.set(p, "456")) === "456")
  }

}
