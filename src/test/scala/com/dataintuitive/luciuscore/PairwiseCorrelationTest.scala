package com.dataintuitive.luciuscore

import com.dataintuitive.luciuscore.PairwiseCorrelation._
import org.scalatest.FlatSpec

class PairwiseCorrelationTest extends FlatSpec {

  "implicit class Point" should "instantiate correctly from a tuple" in {

  }

  "Square constructor" should "correctly generate an integer square" in {
    import com.dataintuitive.luciuscore.PairwiseCorrelation.Point
    implicit val tuples = Numeric[Int]
    AxisAlignedSquare((1, 1), (1, 2), (2, 1), (2, 2))
  }

}
