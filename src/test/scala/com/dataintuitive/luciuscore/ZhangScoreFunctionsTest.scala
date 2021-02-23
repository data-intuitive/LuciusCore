package com.dataintuitive.luciuscore

import com.dataintuitive.luciuscore.Model._
import com.dataintuitive.luciuscore.ZhangScoreFunctions._
import com.dataintuitive.test.BaseSparkContextSpec
import org.scalatest.flatspec.AnyFlatSpec

/**
  * Created by toni on 26/04/16.
  */
class ZhangScoreFunctionsTest extends AnyFlatSpec with BaseSparkContextSpec {

  info("Testing Connection score calculation")

  "Connection score" should "give correct result" in {
    val x: RankVector = Array(3.0, 2.0, 1.0, 0.0)
    val y: RankVector = Array(0.0, 1.0, 2.0, 3.0)
    assert(connectionScore(x,y) === (3.0*0.0 + 2.0*1.0 + 1.0*2.0 + 0.0*3.0)/14.0)
  }

  it should "give zero when one vector contains only 1 gene" in {
    val x: RankVector = Array(3.0, 2.0, 1.0, 0.0)
    val y: RankVector = Array(0.0, 0.0, 0.0, 1.0)
    assert(connectionScore(x,y) === 0.0)
  }

  it should "give 1. for equal vectors" in {
    val x: RankVector = Array(3.0, 2.0, 1.0, 0.0)
    val y: RankVector = Array(3.0, 2.0, 1.0, 0.0)
    assert(connectionScore(x,y) === 1.0)
  }

}
