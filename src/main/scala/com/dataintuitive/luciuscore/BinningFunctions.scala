package com.dataintuitive.luciuscore

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object BinningFunctions {

  /**
    * assumptions: We would like to reduce a scatter plot of many points to a heat map of squares, with each square
    * colored by how many points fall within it, in order to visualise very dense/large datasets.
    */

  case class Coordinate(x: BigDecimal, y: BigDecimal)
  case class Square(leftBottom: Coordinate, leftTop: Coordinate, rightBottom: Coordinate, rightTop: Coordinate)

  /**
    * Checks if a point is inside an n-dimensional cube whose edges are strictly aligned with the axes
    * @param point
    * @param hypercubeEdges
    * @return
    */
  def isInsideAxisAlignedHypercube(point: List[BigDecimal], hypercubeVertices: List[List[BigDecimal]]): Boolean = {
    val dimensionIndex = point.indices
    val verticesByDimension = dimensionIndex.map(index => hypercubeVertices.map{x => x(index)})
    val extrema = verticesByDimension.map(x => (x.min, x.max))
    val dimensionCheck = dimensionIndex
      .map(index => (extrema(index)._1 <= point(index) &&  point(index) <= extrema(index)._2))
    if (dimensionCheck.contains(false)) {
      false
    } else {
      true
    }
  }

  def listsToTuples(xValues: List[BigDecimal], yValues: List[BigDecimal]): List[Coordinate] = {
    require(xValues.size == yValues.size, "Must be an x for every y.")
    (xValues, yValues).zipped.map(Coordinate)
  }

  def tuplesToLists(XY: List[Coordinate]): (List[BigDecimal], List[BigDecimal]) = {
    XY.unzip{case Coordinate(x, y) => (x, y)}
  }

  def generateBottomLeftAndTopRight(xValues: List[BigDecimal], yValues: List[BigDecimal],
                      partitionNum: BigDecimal): List[Seq[Coordinate]] = {
    if (partitionNum == 0) throw new IllegalArgumentException("Can not partition a dimension into 0 intervals.")
    val (xDiff, yDiff) = (xValues.max - xValues.min, yValues.max - yValues.min)
    val (xStepSize, yStepSize) =
      (xDiff.abs/partitionNum, yDiff.abs/partitionNum)
    val (xSteps, ySteps) = (xValues.min to xValues.max by xStepSize toList, yValues.min to yValues.max by yStepSize toList)
    val xStepsSafe = if (!xSteps.contains(xValues.max)) xSteps.init ::: List(xValues.max) else xSteps
    val yStepsSafe = if (!ySteps.contains(yValues.max)) ySteps.init ::: List(yValues.max) else ySteps
    val (xSlide, ySlide) = (xStepsSafe.iterator.sliding(2).toList, yStepsSafe.iterator.sliding(2).toList)
    val asLists = xSlide.flatMap(xWindow => ySlide.map(yWindow => xWindow.zip(yWindow)))
    asLists.map(twoPoints => twoPoints.map(aCoordinate => Coordinate(aCoordinate._1, aCoordinate._2)))
  }

  def imputeTopLeftAndBottomRight(bottomLeft: Coordinate, topRight: Coordinate): Square = {
    Square(bottomLeft, Coordinate(bottomLeft.x, topRight.y), Coordinate(topRight.x, bottomLeft.y), topRight)
  }

  def generateSquares(xValues: List[BigDecimal], yValues: List[BigDecimal], partitionNum: BigDecimal): List[Square] = {
    generateBottomLeftAndTopRight(xValues, yValues, partitionNum).map(incompleteSquare =>
      imputeTopLeftAndBottomRight(incompleteSquare.head, incompleteSquare.last))
  }

  def centroidMapper(squares: List[Square]): Map[Coordinate, Square] = {
    Map(squares.map{square =>
      (Coordinate(square.leftBottom.x + (square.rightBottom.x-square.leftBottom.x)/2,
        square.leftBottom.y + (square.leftTop.y - square.leftBottom.y)/2), square)
    }:_*)
  }

  def centroidMapperRDD(sc: SparkContext, squares: List[Square]): RDD[(Coordinate, Square)] = {
    val squaresRDD = sc.parallelize(squares)
    squaresRDD.map(square => (Coordinate(square.leftBottom.x + (square.rightBottom.x-square.leftBottom.x)/2,
        square.leftBottom.y + (square.leftTop.y - square.leftBottom.y)/2), square))
  }

  def whichSquare(aCoordinate: Coordinate, squaresMap: Map[Coordinate, Square]): (Coordinate, Square) = {
    squaresMap.keysIterator
    (Coordinate(0,0), Square(Coordinate(0, 0),Coordinate(0, 0),Coordinate(0,0),Coordinate(0,0)))
  }


}
