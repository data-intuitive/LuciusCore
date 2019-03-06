package com.dataintuitive.luciuscore

import org.scalatest.FlatSpec
import com.dataintuitive.test.BaseSparkContextSpec
import org.scalatest.Matchers._
import com.dataintuitive.luciuscore.BinningFunctions._

class BinningFunctionsTest extends FlatSpec with BaseSparkContextSpec{

  val X = List(BigDecimal(-1.0), BigDecimal(0.0), BigDecimal(1.0), BigDecimal(2.5))
  val Y = List(BigDecimal(-0.5), BigDecimal(0.5), BigDecimal(2.0), BigDecimal(4.0))

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
    assert(generateBottomLeftAndTopRight(X, Y, 2) ==
      List(List(Coordinate(-1.0,-0.5), Coordinate(0.75,1.75)),
        List(Coordinate(-1.0,1.75), Coordinate(0.75,4.00)),
        List(Coordinate(0.75,-0.5), Coordinate(2.50,1.75)),
        List(Coordinate(0.75,1.75), Coordinate(2.50,4.00))))
  }

  "imputeTopLeftAndBottomRight" should "correctly impute a simple square" in {
    val bottomLeft = Coordinate(BigDecimal(-1.0), BigDecimal(-1.0))
    val topRight = Coordinate(BigDecimal(0.75), BigDecimal(0.75))
    assert(imputeTopLeftAndBottomRight(bottomLeft, topRight) ==
      Square(Coordinate(BigDecimal(-1.0), BigDecimal(-1.0)), Coordinate(BigDecimal(-1.0), BigDecimal(0.75)),
      Coordinate(BigDecimal(0.75), BigDecimal(-1.0)), Coordinate(BigDecimal(0.75), BigDecimal(0.75))))
  }

  val partitions = 2
  val incompleteSquares = generateBottomLeftAndTopRight(X, Y, partitions)
  incompleteSquares shouldBe a [List[_]]
  val completeSquares = incompleteSquares.map(twoCoordinates => imputeTopLeftAndBottomRight(twoCoordinates(0), twoCoordinates(1)))
  completeSquares shouldBe a [List[_]]
  completeSquares.head shouldBe a [Square]
  completeSquares.size shouldBe 4

  "centroidMapper" should "correctly create a Map of square centers and the associated squares" in {
    val square1 = Square(Coordinate(-1.0, 0.75), Coordinate(-1.0, 2.5), Coordinate(0.75, 0.75), Coordinate(0.75, 2.5))
    val square2 = Square(Coordinate(-1.0, -1.0), Coordinate(-1.0, 0.75), Coordinate(0.75, -1.0), Coordinate(0.75, 0.75))
    assert(centroidMapper(List(square1, square2)) == Map(Coordinate(-0.125, 1.625) -> square1, Coordinate(-0.125, -0.125) -> square2))
  }

  "generateSquares" should "correctly generate squares when each dim has two partitions" in {
    val squares = generateSquares(X, Y, 2)
    assert(squares == List(
      Square(Coordinate(-1.0,-0.5),Coordinate(-1.0,1.75),Coordinate(0.75,-0.5),Coordinate(0.75,1.75)),
      Square(Coordinate(-1.0,1.75),Coordinate(-1.0,4.00),Coordinate(0.75,1.75),Coordinate(0.75,4.00)),
      Square(Coordinate(0.75,-0.5),Coordinate(0.75,1.75),Coordinate(2.50,-0.5),Coordinate(2.50,1.75)),
      Square(Coordinate(0.75,1.75),Coordinate(0.75,4.00),Coordinate(2.50,1.75),Coordinate(2.50,4.00))))
  }

  "centroidMapperRDD" should "correctly create a Map of square centers and the associated squares" in {
    val square1 = Square(Coordinate(-1.0, 0.75), Coordinate(-1.0, 2.5), Coordinate(0.75, 0.75), Coordinate(0.75, 2.5))
    val square2 = Square(Coordinate(-1.0, -1.0), Coordinate(-1.0, 0.75), Coordinate(0.75, -1.0), Coordinate(0.75, 0.75))
    val pairsRDD = centroidMapperRDD(sc, List(square1, square2))
    assert(pairsRDD.collectAsMap == Map(Coordinate(-0.125, 1.625) -> square1, Coordinate(-0.125, -0.125) -> square2))
  }

  "whichSquare" should "correctly determine a coordinate is inside a square" in {
    val square1 = Square(Coordinate(-1.0, 0.75), Coordinate(-1.0, 2.5), Coordinate(0.75, 0.75), Coordinate(0.75, 2.5))
    val square2 = Square(Coordinate(-1.0, -1.0), Coordinate(-1.0, 0.75), Coordinate(0.75, -1.0), Coordinate(0.75, 0.75))
    val squareCentroidMap = Map(Coordinate(-0.125, 1.625) -> square1, Coordinate(-0.125, -0.125) -> square2)
    val coordinateInsideSquare1 = Coordinate(0, 1.5)
    assert(whichSquare(coordinateInsideSquare1, squareCentroidMap) == (coordinateInsideSquare1, Option(square1)))
  }



}
