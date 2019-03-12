package com.dataintuitive.luciuscore

import org.scalatest.{FlatSpec, PrivateMethodTester}
import com.dataintuitive.test.BaseSparkContextSpec
import org.scalatest.Matchers._
import com.dataintuitive.luciuscore.ZhangScorePair._

class ZhangScorePairTest extends FlatSpec with BaseSparkContextSpec with PrivateMethodTester{

  val X = Vector(BigDecimal(-1.0), BigDecimal(0.0), BigDecimal(1.0), BigDecimal(2.5))
  val Y = Vector(BigDecimal(-0.5), BigDecimal(0.5), BigDecimal(2.0), BigDecimal(4.0))
  val pair1 = new ZhangScorePair(X, Y)

  "ZhangScorePair primary constructor" should "correctly convert X vector and Y vector to (X, Y)" in {
    assert(pair1.XY == Vector(Coordinate(-1.0, -0.5), Coordinate(0.0, 0.5),
      Coordinate(1.0, 2.0), Coordinate(2.5, 4.0)))
  }

  "group of coordinates" should "correctly transform from (X, Y) into two vectors using toTuples" in {
    val coordVec = Vector(Coordinate(-1.0, -0.5), Coordinate(0.0, 0.5),
      Coordinate(1.0, 2.0), Coordinate(2.5, 4.0))
    assert(coordVec.map(_.toTuple).unzip == (X, Y))
  }

  "Square case class" should "correctly compute centers " in {
    val square1 = Square(Coordinate(-1.0, 0.75), Coordinate(-1.0, 2.5), Coordinate(0.75, 0.75), Coordinate(0.75, 2.5))
    val square2 = Square(Coordinate(-1.0, -1.0), Coordinate(-1.0, 0.75), Coordinate(0.75, -1.0), Coordinate(0.75, 0.75))
    assert(Vector(square1.center, square2.center) ==
      Vector(Coordinate(-0.125, 1.625), Coordinate(-0.125, -0.125)))
  }

  "generateSquares" should "correctly generate squares when each dim has two partitions" in {
    val squares = pair1.generateSquares(2).get
    assert(squares == Vector(
      Square(Coordinate(-1.0,-0.5),Coordinate(-1.0,1.75),Coordinate(0.75,-0.5),Coordinate(0.75,1.75)),
      Square(Coordinate(-1.0,1.75),Coordinate(-1.0,4.00),Coordinate(0.75,1.75),Coordinate(0.75,4.00)),
      Square(Coordinate(0.75,-0.5),Coordinate(0.75,1.75),Coordinate(2.50,-0.5),Coordinate(2.50,1.75)),
      Square(Coordinate(0.75,1.75),Coordinate(0.75,4.00),Coordinate(2.50,1.75),Coordinate(2.50,4.00))))
  }

  "whichSquare" should "gracefully return None if coordinate is outside a square" in {
    val square1 = Square(Coordinate(-1.0, 0.75), Coordinate(-1.0, 2.5), Coordinate(0.75, 0.75), Coordinate(0.75, 2.5))
    val square2 = Square(Coordinate(-1.0, -1.0), Coordinate(-1.0, 0.75), Coordinate(0.75, -1.0), Coordinate(0.75, 0.75))
    val coordinateOutsideSquare1 = Coordinate(-6, -9)
    assert(whichSquare(coordinateOutsideSquare1, Vector(square1, square2)).isEmpty)
  }

  "whichSquare" should "correctly determine a coordinate is inside a square" in {
    val square1 = Square(Coordinate(-1.0, 0.75), Coordinate(-1.0, 2.5), Coordinate(0.75, 0.75), Coordinate(0.75, 2.5))
    val square2 = Square(Coordinate(-1.0, -1.0), Coordinate(-1.0, 0.75), Coordinate(0.75, -1.0), Coordinate(0.75, 0.75))
    val coordinateInsideSquare1 = Coordinate(0, 1.5)
    assert(whichSquare(coordinateInsideSquare1, Vector(square1, square2)).get == (coordinateInsideSquare1, square1))
  }

  "assignCoordinatesToSquares" should "correctly assign a group of points to a group of squares" in {
    val square1 = Square(Coordinate(-1.0, 0.75), Coordinate(-1.0, 2.5), Coordinate(0.75, 0.75), Coordinate(0.75, 2.5))
    val square2 = Square(Coordinate(-1.0, -1.0), Coordinate(-1.0, 0.75), Coordinate(0.75, -1.0), Coordinate(0.75, 0.75))
    val coordList = Vector(Coordinate(0, 1.5), Coordinate(0, 0))
    assert(assignCoordinatesToSquares(sc, coordList, Vector(square1, square2)).collect.toVector ==
      Vector((Square(Coordinate(-1.0,0.75),Coordinate(-1.0,2.5),Coordinate(0.75,0.75),Coordinate(0.75,2.5)), Coordinate(0,1.5)),
        (Square(Coordinate(-1.0,-1.0),Coordinate(-1.0,0.75),Coordinate(0.75,-1.0),Coordinate(0.75,0.75)), Coordinate(0,0))))
  }

  "squaresWithAllPoints" should "correctly aggregate every coordinate into vectors in their squares" in {
    val squaresAndPoints = Vector((Square(Coordinate(-1.0,0.75),Coordinate(-1.0,2.5),Coordinate(0.75,0.75),Coordinate(0.75,2.5)), Coordinate(0,1.5)),
    (Square(Coordinate(-1.0,-1.0),Coordinate(-1.0,0.75),Coordinate(0.75,-1.0),Coordinate(0.75,0.75)), Coordinate(0,0)),
      (Square(Coordinate(-1.0,-1.0),Coordinate(-1.0,0.75),Coordinate(0.75,-1.0),Coordinate(0.75,0.75)), Coordinate(0.01,0.01)))
    val squaresAndPointsRDD = sc.parallelize(squaresAndPoints)
    val res1 = squaresWithAllPoints(sc, squaresAndPointsRDD).collectAsMap
    assert(res1 ==  Map(Square(Coordinate(-1.0,0.75),Coordinate(-1.0,2.5),Coordinate(0.75,0.75),Coordinate(0.75,2.5)) -> Vector(Coordinate(0,1.5)),
      Square(Coordinate(-1.0,-1.0),Coordinate(-1.0,0.75),Coordinate(0.75,-1.0),Coordinate(0.75,0.75)) -> Vector(Coordinate(0,0),Coordinate(0.01,0.01))))
  }

}
