package com.dataintuitive.luciuscore

import com.dataintuitive.luciuscore.PairwiseCorrelation._
import org.scalatest.FlatSpec

object PairwiseCorrelationTest extends FlatSpec {

  "Square constructor" should "correctly generate an integer square" in {
    AxisAlignedSquare((1, 1),  (1, 2), (2, 1), (2, 2))
  }

}
