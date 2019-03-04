package com.dataintuitive.luciuscore

import org.scalatest.FlatSpec
import com.dataintuitive.test.BaseSparkContextSpec
import org.scalatest.Matchers._
import com.dataintuitive.luciuscore.BinningFunctions._

import scala.math.abs

class BinningFunctionsTest extends FlatSpec with BaseSparkContextSpec{

  val X = List(BigDecimal(-1.0), BigDecimal(0.0), BigDecimal(1.0), BigDecimal(2.5))
  val Y = List(BigDecimal(-0.5), BigDecimal(0.5), BigDecimal(2.0), BigDecimal(4.0))
  /**
  "generateSquares function" should "create a list of squares, themselves lists of length 4, " +
    "defined by their vertices, which should be x, y coordinate tuples" in {
    assert(generateSquares(X, Y, 1) ==
      List(List(List(-1.0, -0.5), List(-1.0, 4.0), List(2.5, -0.5), List(2.5, 4.0))))
  }*/

  "listsToTuples" should "correctly convert X vector and Y vector to (X, Y)" in {
    assert(listsToTuples(X, Y) == List(Coordinate(-1.0, -0.5), Coordinate(0.0, 0.5),
      Coordinate(1.0, 2.0), Coordinate(2.5, 4.0)))
  }

  "tuplesToLists" should "correctly transform (X, Y) into two vectors" in {
    assert(tuplesToLists(List(Coordinate(-1.0, -0.5), Coordinate(0.0, 0.5),
      Coordinate(1.0, 2.0), Coordinate(2.5, 4.0))) == (X, Y))
  }

  "generateBottomLeftAndTopRight" should "correctly return the coordinates of the bottom left and top right " +
    "corners of the smallest set of squares" in {
    // lower than 5 partitions will generate a non-inclusive range due to arithmetic errors
    assert(generateBottomLeftAndTopRight(X, Y, 2) ==
      List(Seq(Coordinate(-1.0, -0.5), Coordinate(0.75, 1.25)), Seq(Coordinate(-1.0, 1.25), Coordinate(0.75, 4.00)),
        Seq(Coordinate(0.75, -0.5), Coordinate(2.5, 1.25)), Seq(Coordinate(0.75, 1.25), Coordinate(2.5, 4.00))))
  }

  "imputeTopLeftAndBottomRight" should "correctly impute a simple square" in {
    val bottomLeft = Coordinate(BigDecimal(-1.0), BigDecimal(-1.0))
    val topRight = Coordinate(BigDecimal(0.75), BigDecimal(0.75))
    imputeTopLeftAndBottomRight(bottomLeft, topRight) ==
      Square(Coordinate(BigDecimal(-1.0), BigDecimal(-1.0)), Coordinate(BigDecimal(-1.0), BigDecimal(0.75)),
      Coordinate(BigDecimal(0.75), BigDecimal(-1.0)), Coordinate(BigDecimal(0.75), BigDecimal(0.75)))
  }

  val incompleteSquares = generateBottomLeftAndTopRight(X, Y, 5)
  incompleteSquares shouldBe a [List[_]]

  val completeSquares = incompleteSquares.map(twoCoordinates => imputeTopLeftAndBottomRight(twoCoordinates(0), twoCoordinates(1)))
  completeSquares shouldBe a [List[_]]
  completeSquares.head shouldBe a [Square]

}
