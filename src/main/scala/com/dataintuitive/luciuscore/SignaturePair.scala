package com.dataintuitive.luciuscore

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD


object SignaturePair {
  case class Coordinate(x: BigDecimal, y: BigDecimal) {
    def toTuple: (BigDecimal, BigDecimal) = {
      (this.x, this.y)
    }
  }
  case class Square(leftBottom: Coordinate, leftTop: Coordinate, rightBottom: Coordinate, rightTop: Coordinate) {
    def squareToXY: (Vector[BigDecimal], Vector[BigDecimal]) = {
      (Vector(this.leftBottom.x, this.rightTop.x), Vector(this.leftBottom.y, this.rightTop.y))
    }
  }
}

sealed class SignaturePair(X: Vector[BigDecimal], Y: Vector[BigDecimal]) extends Serializable {

  import SignaturePair._
  val XY: Vector[Coordinate] = (this.X, this.Y).zipped.map(Coordinate)

  def isInsideSquare(point: Coordinate, square: Square): Boolean = {
    val (xSquare, ySquare) = square.squareToXY
    if ((xSquare.min <= point.x && point.x <= xSquare.max) &&
      (ySquare.min <= point.y && point.y <= ySquare.max)) true
    else false
  }

  def generateSquares(xValues: Vector[BigDecimal], yValues: Vector[BigDecimal],
                      partitionNum: BigDecimal): Option[Vector[Square]] = {
    if (partitionNum <= 0) None
    else Some{
      val (xDiff, yDiff) = (xValues.max - xValues.min, yValues.max - yValues.min)
      val (xStepSize, yStepSize) =
        (xDiff.abs/partitionNum, yDiff.abs/partitionNum)
      val (xSteps, ySteps) = (xValues.min to xValues.max by xStepSize toList, yValues.min to yValues.max by yStepSize toList)
      val xStepsSafe = if (!xSteps.contains(xValues.max)) xSteps.init ::: List(xValues.max) else xSteps
      val yStepsSafe = if (!ySteps.contains(yValues.max)) ySteps.init ::: List(yValues.max) else ySteps
      val (xSlide, ySlide) = (xStepsSafe.iterator.sliding(2).toVector, yStepsSafe.iterator.sliding(2).toVector)
      xSlide.flatMap(xWindow => ySlide.map(yWindow => xWindow.zip(yWindow)))
        .map(twoPoints => twoPoints.map(aCoordinate => Coordinate(aCoordinate._1, aCoordinate._2)).toVector)
        .map(incompleteSquare =>
        Square(incompleteSquare.head, Coordinate(incompleteSquare.head.x, incompleteSquare.last.y),
          Coordinate(incompleteSquare.last.x, incompleteSquare.head.y), incompleteSquare.last))
    }
  }

  def centroidMapper(squares: Vector[Square]): Map[Coordinate, Square] = {
    Map(squares.map{square =>
      (Coordinate(square.leftBottom.x + (square.rightBottom.x-square.leftBottom.x)/2,
        square.leftBottom.y + (square.leftTop.y - square.leftBottom.y)/2), square)
    }:_*)
  }

  def centroidMapperRDD(sc: SparkContext, squares: Vector[Square]): RDD[(Coordinate, Square)] = {
    val squaresRDD = sc.parallelize(squares)
    squaresRDD.map(square => (Coordinate(square.leftBottom.x + (square.rightBottom.x-square.leftBottom.x)/2,
        square.leftBottom.y + (square.leftTop.y - square.leftBottom.y)/2), square))
  }

  def whichSquare(aCoordinate: Coordinate, squares: Vector[Square]):
  Option[(Coordinate, Square)] = {
    val matchingSquares = squares.filter(isInsideSquare(aCoordinate, _))
    if (matchingSquares.length != 1) None
    else Some {
      (aCoordinate, matchingSquares.head)
    }
  }

  def assignCoordinatesToSquares(sc: SparkContext, coordinateList: Vector[Coordinate],
                                 squares: Vector[Square]): RDD[(Square,Coordinate)] = {
    sc.parallelize(coordinateList).map{aCoordinate => whichSquare(aCoordinate, squares)}
      .map(binnedCoordinateTuple => (binnedCoordinateTuple.get._2, binnedCoordinateTuple.get._1 ))
  }

  def squaresWithAllPoints(sc: SparkContext, binnedCoords: RDD[(Square, Coordinate)]): RDD[(Square, Vector[Coordinate])] = {
    val addFunction = (vector1: Vector[Coordinate], coord: Coordinate) => vector1++Vector(coord)
    val mergeFunction = (vector1: Vector[Coordinate], vector2: Vector[Coordinate]) => vector1++vector2
    binnedCoords.aggregateByKey(Vector[Coordinate]())(addFunction, mergeFunction)
  }

}
