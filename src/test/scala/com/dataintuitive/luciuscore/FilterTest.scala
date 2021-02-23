package com.dataintuitive.luciuscore

import com.dataintuitive.luciuscore.model.v3.Filter
import com.dataintuitive.luciuscore.model.v3.QFilter
import com.dataintuitive.luciuscore.model.v3.FilterFunctions._

import org.scalatest.flatspec.AnyFlatSpec

class FilterTest extends AnyFlatSpec {

  val f1a = Filter("filter1", "value1")
  val f1b = Filter("filter1", "value2")
  val f2a = Filter("filter2", "value2")
  val f2b = Filter("filter2", "value1")

  val qFilter1 = QFilter("filter1", Seq("value1", "value2", "value3"))
  val qFilter2 = QFilter("filter2", Seq("value1"))
  val qFilter1NoMatch = QFilter("filter2", Seq("value3"))

  val filters = Seq(f1a, f1b, f2a, f2b)

  "isMatch on 1 QFilter" should "return the expected result" in {
    assert(isMatch(qFilter1, filters) === true)
    assert(isMatch(qFilter2, filters) === true)
    assert(isMatch(qFilter1NoMatch, filters) === false)
  }

  "isMatch on Seq[QFilter]" should "return the expected result" in {
    assert(isMatch(Seq(qFilter1, qFilter2), filters) === true)
    assert(isMatch(Seq(qFilter1NoMatch, qFilter2), filters) === false)
  }

  "Edge cases" should "behave as expected" in {
    // Empty filters, there can be no match
    assert(isMatch(Seq(qFilter1, qFilter2), Seq()) === false)
    // Empty qfilters, no filters means there is a match
    assert(isMatch(Seq(), filters) === true)
  }

}
