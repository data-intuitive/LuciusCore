package com.dataintuitive.luciuscore.utilities

import com.dataintuitive.luciuscore.utilities.Statistics._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/**
  * Created by toni on 12/09/16.
  */
class StatisticsTest extends AnyFunSuite with Matchers {

  test("Median for odd number of elements") {
    val v = Seq(-5.0, 3.0, -1.0, 0.0, 2.0)
    assert(median(v) === 0.0)
  }

  test("Median for even number of elements") {
    val v = Seq(-5.0, 5.0, -1.0, 1.0)
    assert(median(v) === 0.0)
  }

  test("Median for zero elements returns IllegalArgumentException") {
    val v = Seq()
    val thrown = the [java.lang.IllegalArgumentException] thrownBy (median(v))
    thrown should have message "requirement failed: The median of a zero-length list is not defined"
  }

}
