package com.dataintuitive.luciuscore

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD


object SignaturePair {
  case class Coordinate(x: BigDecimal, y: BigDecimal)
  case class Square(leftBottom: Coordinate, leftTop: Coordinate, rightBottom: Coordinate, rightTop: Coordinate)
  case class BinnedCoordinate(theCoordinate: Coordinate, containingSquare: Square)
}

sealed class SignaturePair(X: Vector[BigDecimal], Y: Vector[BigDecimal]) extends Serializable {

  import SignaturePair._
  /**
    * assumptions: We would like to reduce a scatter plot of many points to a heat map of squares, with each square
    * colored by how many points fall within it, in order to visualise very dense/large datasets.
    */

  def squareToXY(square: Square): (Vector[BigDecimal], Vector[BigDecimal]) = {
    (Vector(square.leftBottom.x, square.rightTop.x), Vector(square.leftBottom.y, square.rightTop.y))
  }

  def listsToTuples(xValues: Vector[BigDecimal], yValues: Vector[BigDecimal]): Vector[Coordinate] = {
    require(xValues.size == yValues.size, "Must be an x for every y.")
    (xValues, yValues).zipped.map(Coordinate)
  }

  def tuplesToLists(XY: Vector[Coordinate]): (Vector[BigDecimal], Vector[BigDecimal]) = {
    XY.unzip{case Coordinate(x, y) => (x, y)}
  }

  val XY: Vector[Coordinate] = this.listsToTuples(X, Y)

  def isInsideSquare(point: Coordinate, square: Square): Boolean = {
    val (xSquare, ySquare) = squareToXY(square)
    if ((xSquare.min <= point.x && point.x <= xSquare.max) &&
      (ySquare.min <= point.y && point.y <= ySquare.max)) true
    else false
  }

  def generateSquares(xValues: Vector[BigDecimal], yValues: Vector[BigDecimal],
                      partitionNum: BigDecimal): Vector[Square] = {
    if (partitionNum == 0) throw new IllegalArgumentException("Can not partition a dimension into 0 intervals.")
    val (xDiff, yDiff) = (xValues.max - xValues.min, yValues.max - yValues.min)
    val (xStepSize, yStepSize) =
      (xDiff.abs/partitionNum, yDiff.abs/partitionNum)
    val (xSteps, ySteps) = (xValues.min to xValues.max by xStepSize toList, yValues.min to yValues.max by yStepSize toList)
    val xStepsSafe = if (!xSteps.contains(xValues.max)) xSteps.init ::: List(xValues.max) else xSteps
    val yStepsSafe = if (!ySteps.contains(yValues.max)) ySteps.init ::: List(yValues.max) else ySteps
    val (xSlide, ySlide) = (xStepsSafe.iterator.sliding(2).toVector, yStepsSafe.iterator.sliding(2).toVector)
    val asLists = xSlide.flatMap(xWindow => ySlide.map(yWindow => xWindow.zip(yWindow)))
    val incompleteSquares = asLists.map(twoPoints => twoPoints.map(aCoordinate => Coordinate(aCoordinate._1, aCoordinate._2)).toVector)
    incompleteSquares.map(incompleteSquare =>
      Square(incompleteSquare.head, Coordinate(incompleteSquare.head.x, incompleteSquare.last.y),
        Coordinate(incompleteSquare.last.x, incompleteSquare.head.y), incompleteSquare.last))
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

  def whichSquare(aCoordinate: Coordinate, squaresMap: Map[Coordinate, Square]): (Coordinate, Option[Square]) = {
    val matchingSquareKeyList = squaresMap.keysIterator.takeWhile(square =>
      isInsideSquare(aCoordinate, squaresMap(square))).toList
    if (matchingSquareKeyList.size > 1) throw new IllegalArgumentException("Point belonging to more than one square!")
    // TODO: Option to replace sentinel value -999, -999
    val matchingSquareKey = matchingSquareKeyList.headOption.getOrElse(Coordinate(-999,-999))
    try {
    (aCoordinate, squaresMap.get(matchingSquareKey))
    } catch {
      case noSuchElementException: NoSuchElementException => (aCoordinate, None)
    }
  }

  def whichSquareRDD(sc: SparkContext, aCoordinate: Coordinate, squaresMap: Map[Coordinate, Square]):
  (Coordinate, Square) = {
    val squaresMapRDD = sc.parallelize(squaresMap.toSeq)
    val matchingSquareKeyList = squaresMapRDD.filter(square => isInsideSquare(aCoordinate, square._2)).collect
    if (matchingSquareKeyList.length > 1) throw new IllegalArgumentException("Point belonging to more than one square!")
    // this returns the centroid of the square with the square but what we want is the coordinate with the square
    val MatchingSquare = matchingSquareKeyList.headOption.get
    (aCoordinate, MatchingSquare._2)
  }

  def assignCoordinatesToSquares(sc: SparkContext, coordinateList: Vector[Coordinate],
                                 squaresMap: Map[Coordinate, Square]): Vector[BinnedCoordinate] = {
    val listOfBinnedCoordinates:Vector[(Coordinate, Square)] =  coordinateList.map{
      aCoordinate => whichSquareRDD(sc, aCoordinate, squaresMap)
    }
    listOfBinnedCoordinates.map(binnedCoordinateTuple =>
      BinnedCoordinate(binnedCoordinateTuple._1, binnedCoordinateTuple._2))
  }

}
