package com.dataintuitive.luciuscore

import org.scalatest.{FlatSpec, PrivateMethodTester}
import com.dataintuitive.test.BaseSparkContextSpec
import org.scalatest.Matchers._
import com.dataintuitive.luciuscore.SignaturePair._

class SignaturePairTest extends FlatSpec with BaseSparkContextSpec with PrivateMethodTester{

  val X = Vector(BigDecimal(-1.0), BigDecimal(0.0), BigDecimal(1.0), BigDecimal(2.5))
  val Y = Vector(BigDecimal(-0.5), BigDecimal(0.5), BigDecimal(2.0), BigDecimal(4.0))
  val pair1 = new SignaturePair(X, Y)

  "listsToTuples" should "correctly convert X vector and Y vector to (X, Y)" in {
    assert(pair1.listsToTuples(X, Y) == Vector(Coordinate(-1.0, -0.5), Coordinate(0.0, 0.5),
      Coordinate(1.0, 2.0), Coordinate(2.5, 4.0)))
  }

  "tuplesToLists" should "correctly transform (X, Y) into two vectors" in {
    assert(pair1.tuplesToLists(Vector(Coordinate(-1.0, -0.5), Coordinate(0.0, 0.5),
      Coordinate(1.0, 2.0), Coordinate(2.5, 4.0))) == (X, Y))
  }

  "centroidMapper" should "correctly create a Map of square centers and the associated squares" in {
    val square1 = Square(Coordinate(-1.0, 0.75), Coordinate(-1.0, 2.5), Coordinate(0.75, 0.75), Coordinate(0.75, 2.5))
    val square2 = Square(Coordinate(-1.0, -1.0), Coordinate(-1.0, 0.75), Coordinate(0.75, -1.0), Coordinate(0.75, 0.75))
    assert(pair1.centroidMapper(Vector(square1, square2)) ==
      Map(Coordinate(-0.125, 1.625) -> square1, Coordinate(-0.125, -0.125) -> square2))
  }

  "generateSquares" should "correctly generate squares when each dim has two partitions" in {
    val squares = pair1.generateSquares(X, Y, 2)
    assert(squares == Vector(
      Square(Coordinate(-1.0,-0.5),Coordinate(-1.0,1.75),Coordinate(0.75,-0.5),Coordinate(0.75,1.75)),
      Square(Coordinate(-1.0,1.75),Coordinate(-1.0,4.00),Coordinate(0.75,1.75),Coordinate(0.75,4.00)),
      Square(Coordinate(0.75,-0.5),Coordinate(0.75,1.75),Coordinate(2.50,-0.5),Coordinate(2.50,1.75)),
      Square(Coordinate(0.75,1.75),Coordinate(0.75,4.00),Coordinate(2.50,1.75),Coordinate(2.50,4.00))))
  }

  "centroidMapperRDD" should "correctly create a Map of square centers and the associated squares" in {
    val square1 = Square(Coordinate(-1.0, 0.75), Coordinate(-1.0, 2.5), Coordinate(0.75, 0.75), Coordinate(0.75, 2.5))
    val square2 = Square(Coordinate(-1.0, -1.0), Coordinate(-1.0, 0.75), Coordinate(0.75, -1.0), Coordinate(0.75, 0.75))
    val pairsRDD = pair1.centroidMapperRDD(sc, Vector(square1, square2))
    assert(pairsRDD.collectAsMap == Map(Coordinate(-0.125, 1.625) -> square1, Coordinate(-0.125, -0.125) -> square2))
  }

  "whichSquare" should "correctly determine a coordinate is inside a square" in {
    val square1 = Square(Coordinate(-1.0, 0.75), Coordinate(-1.0, 2.5), Coordinate(0.75, 0.75), Coordinate(0.75, 2.5))
    val square2 = Square(Coordinate(-1.0, -1.0), Coordinate(-1.0, 0.75), Coordinate(0.75, -1.0), Coordinate(0.75, 0.75))
    val squareCentroidMap = Map(Coordinate(-0.125, 1.625) -> square1, Coordinate(-0.125, -0.125) -> square2)
    val coordinateInsideSquare1 = Coordinate(0, 1.5)
    assert(pair1.whichSquare(coordinateInsideSquare1, squareCentroidMap) == (coordinateInsideSquare1, Option(square1)))
  }

  "whichSquare" should "gracefully return None if coordinate is outside a square" in {
    val square1 = Square(Coordinate(-1.0, 0.75), Coordinate(-1.0, 2.5), Coordinate(0.75, 0.75), Coordinate(0.75, 2.5))
    val square2 = Square(Coordinate(-1.0, -1.0), Coordinate(-1.0, 0.75), Coordinate(0.75, -1.0), Coordinate(0.75, 0.75))
    val squareCentroidMap = Map(Coordinate(-0.125, 1.625) -> square1, Coordinate(-0.125, -0.125) -> square2)
    val coordinateOutsideSquare1 = Coordinate(-6, -9)
    assert(pair1.whichSquare(coordinateOutsideSquare1, squareCentroidMap) == (coordinateOutsideSquare1, None))
  }

  "whichSquareRDD" should "correctly determine a coordinate is inside a square" in {
    val square1 = Square(Coordinate(-1.0, 0.75), Coordinate(-1.0, 2.5), Coordinate(0.75, 0.75), Coordinate(0.75, 2.5))
    val square2 = Square(Coordinate(-1.0, -1.0), Coordinate(-1.0, 0.75), Coordinate(0.75, -1.0), Coordinate(0.75, 0.75))
    val squareCentroidMap = Map(Coordinate(-0.125, 1.625) -> square1, Coordinate(-0.125, -0.125) -> square2)
    val coordinateInsideSquare1 = Coordinate(0, 1.5)
    assert(pair1.whichSquareRDD(sc, coordinateInsideSquare1, squareCentroidMap) == (coordinateInsideSquare1, square1))
  }

  "assignCoordinatesToSquares" should "correctly assign a group of points to a group of squares" in {
    val square1 = Square(Coordinate(-1.0, 0.75), Coordinate(-1.0, 2.5), Coordinate(0.75, 0.75), Coordinate(0.75, 2.5))
    val square2 = Square(Coordinate(-1.0, -1.0), Coordinate(-1.0, 0.75), Coordinate(0.75, -1.0), Coordinate(0.75, 0.75))
    val squareCentroidMap = Map(Coordinate(-0.125, 1.625) -> square1, Coordinate(-0.125, -0.125) -> square2)
    val coordList = Vector(Coordinate(0, 1.5), Coordinate(0, 0))
    assert(pair1.assignCoordinatesToSquares(sc, coordList, squareCentroidMap) ==
      Vector(BinnedCoordinate(Coordinate(0,1.5),
        Square(Coordinate(-1.0,0.75),Coordinate(-1.0,2.5),Coordinate(0.75,0.75),Coordinate(0.75,2.5))),
        BinnedCoordinate(Coordinate(0,0),
          Square(Coordinate(-1.0,-1.0),Coordinate(-1.0,0.75),Coordinate(0.75,-1.0),Coordinate(0.75,0.75)))))
  }

}
