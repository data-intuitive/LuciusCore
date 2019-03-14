package com.dataintuitive.luciuscore

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD


object ZhangScorePair {

  case class Point(x: BigDecimal, y: BigDecimal) {
    def toTuple: (BigDecimal, BigDecimal) = {
      (this.x, this.y)
    }
  }

  case class Square(leftBottom: Point, leftTop: Point, rightBottom: Point, rightTop: Point) {
    val center = Point(this.leftBottom.x + (this.rightBottom.x-this.leftBottom.x)/2,
      this.leftBottom.y + (this.leftTop.y - this.leftBottom.y)/2)
    def toXY: (Vector[BigDecimal], Vector[BigDecimal]) = {
      (Vector(this.leftBottom.x, this.rightTop.x), Vector(this.leftBottom.y, this.rightTop.y))
    }
  }

  def isInsideSquare(point: Point, square: Square): Boolean = {
    val (xSquare, ySquare) = square.toXY
    if ((xSquare.min <= point.x && point.x <= xSquare.max) &&
      (ySquare.min <= point.y && point.y <= ySquare.max)) true
    else false
  }

  def whichSquare(aCoordinate: Point, squares: Vector[Square]):
  Option[(Point, Square)] = {
    val matchingSquares = squares.filter(isInsideSquare(aCoordinate, _))
    if (matchingSquares.length != 1) None
    else Some {
      (aCoordinate, matchingSquares.head)
    }
  }

  def assignCoordinatesToSquares(sc: SparkContext, coordinateList: RDD[Point],
                                 squares: Vector[Square]): RDD[(Square,Point)] = {
    coordinateList
      .map{aCoordinate => whichSquare(aCoordinate, squares)}
      .map(binnedCoordinateTuple => (binnedCoordinateTuple.get._2, binnedCoordinateTuple.get._1 ))
  }

  def squaresWithAllPoints(sc: SparkContext, binnedCoords: RDD[(Square, Point)]): RDD[(Square, Vector[Point])] = {
    val addFunction = (vector1: Vector[Point], coord: Point) => vector1++Vector(coord)
    val mergeFunction = (vector1: Vector[Point], vector2: Vector[Point]) => vector1++vector2
    binnedCoords.aggregateByKey(Vector[Point]())(addFunction, mergeFunction)
  }

}

sealed class ZhangScorePair(X: RDD[BigDecimal], Y: RDD[BigDecimal]) extends Serializable {

  import ZhangScorePair._
  val XY: RDD[Point] = this.X zip this.Y map{ case (x, y) => Point(x, y)}

  def generateSquares(partitionNum: BigDecimal): Option[Vector[Square]] = {
    if (partitionNum <= 0) None
    else Some{
      val (xMin, xMax, yMin, yMax) = (this.X.min, this.X.max, this.Y.min, this.Y.max)
      val (xDiff, yDiff) = (xMax - xMin, yMax - yMin)
      val (xStepSize, yStepSize) =
        (xDiff.abs/partitionNum, yDiff.abs/partitionNum)
      val (xSteps, ySteps) = (xMin to xMax by xStepSize toList, yMin to yMax by yStepSize toList)
      val xStepsSafe = if (!xSteps.contains(xMax)) xSteps.init ::: List(xMax) else xSteps
      val yStepsSafe = if (!ySteps.contains(yMax)) ySteps.init ::: List(yMax) else ySteps
      val (xSlide, ySlide) = (xStepsSafe.iterator.sliding(2).toVector, yStepsSafe.iterator.sliding(2).toVector)
      xSlide.flatMap(xWindow => ySlide.map(yWindow => xWindow.zip(yWindow)))
        .map(twoPoints => twoPoints.map(aCoordinate => Point(aCoordinate._1, aCoordinate._2)).toVector)
        .map(incompleteSquare =>
        Square(incompleteSquare.head, Point(incompleteSquare.head.x, incompleteSquare.last.y),
          Point(incompleteSquare.last.x, incompleteSquare.head.y), incompleteSquare.last))
    }
  }

}
