package com.dataintuitive.luciuscore.utilities

import org.scalatest.flatspec.AnyFlatSpec


class SignedStringTest extends AnyFlatSpec {

  info("Test String extensions")

  val aString = "-aString"
  val extraStringExpl = new SignedString("-aString")

  "Explicit creation of ExtraString" should "work" in {
    assert(extraStringExpl.string === aString)
  }

  "abs on ExtraString" should "remove return the string with trailing - removed" in {
    assert(extraStringExpl.abs === "aString")
  }

  "sign on ExtraString" should "remove return the sign" in {
    assert(extraStringExpl.sign === "-")
  }


}
