package com.dataintuitive.luciuscore
package model.v4
package lenses

import filters._
import lenses.PerturbationLenses._

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should._

class LensesTestv4 extends AnyFlatSpec with Matchers {

  val p = Perturbation("123", Information(), Profiles(), TRT_EMPTY, Nil)

  "Minimal Instantiation" should "simply work" in {
    assert(p.id === "123")
  }

  it should "work with lenses for first-level attributes" in {
    val thisLens = PerturbationLenses.idLens
    assert(thisLens.get(p) === "123")
    assert(thisLens.get(thisLens.set(p, "456")) === "456")
  }

  it should "work with lenses for nested attributes" in {
    val profilesLens = PerturbationLenses.profilesLens
    val filtersLens = PerturbationLenses.filtersLens
    val trtTLens = PerturbationLenses.trtLens
    profilesLens.get(p) shouldBe a [Profiles]
    filtersLens.get(p) shouldBe a [Filters]
    trtLens.get(p) shouldBe a [Treatment]
  }

}
