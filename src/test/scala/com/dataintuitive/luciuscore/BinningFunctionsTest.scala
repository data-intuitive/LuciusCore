package com.dataintuitive.luciuscore

import org.scalatest.FlatSpec
import com.dataintuitive.test.BaseSparkContextSpec
import org.scalatest.{FlatSpec, Matchers}
import com.dataintuitive.luciuscore.BinningFunctions._

import scala.math.abs

class BinningFunctionsTest extends FlatSpec with BaseSparkContextSpec{

  val X = List(-1.0, 0.0, 1.0, 2.5)
  val Y = List(-0.5, 0.5, 2.0, 4.0)

  "generateSquares function" should "create a list of squares, themselves lists of length 4, " +
    "defined by their vertices, which should be x, y coordinate tuples" in {
    assert(generateSquares(X, Y, 1) ==
      List(List(List(-1.0, -0.5), List(-1.0, 4.0), List(2.5, -0.5), List(2.5, 4.0))))
  }

  "generateSquares function" should "correctly split a space into four squares" in {
    assert(generateSquares(X, Y, 2) == List())
  }

  "listsToTuples" should "correctly convert X vector and Y vector to (X, Y)" in {
    assert(listsToTuples(X, Y) == List(Coordinate(-1.0, -0.5), Coordinate(0.0, 0.5),
      Coordinate(1.0, 2.0), Coordinate(2.5, 4.0)))
  }

  "tuplesToLists" should "correctly transform (X, Y) into two vectors" in {
    assert(tuplesToLists(List(Coordinate(-1.0, -0.5), Coordinate(0.0, 0.5),
      Coordinate(1.0, 2.0), Coordinate(2.5, 4.0))) == (X, Y))
  }

}
