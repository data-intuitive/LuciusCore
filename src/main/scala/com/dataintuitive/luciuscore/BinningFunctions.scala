package com.dataintuitive.luciuscore

import com.sun.javaws.exceptions.InvalidArgumentException

import scala.math.{pow, sqrt, abs}

object BinningFunctions {

  /**
    * assumptions: We would like to reduce a scatter plot of many points to a heat map of squares, with each square
    * colored by how many points fall within it, in order to visualise very dense/large datasets.
    */

  case class Coordinate(x: Double, y: Double)
  case class Square(leftBottom: Double, leftTop: Double, rightBottom: Double, rightTop: Double)

  /**
    * Checks if a point is inside an n-dimensional cube whose edges are strictly aligned with the axes
    * @param point
    * @param hypercubeEdges
    * @return
    */
  def isInsideAxisAlignedHypercube(point: List[Double], hypercubeVertices: List[List[Double]]): Boolean = {
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

  def listsToTuples(xValues: List[Double], yValues: List[Double]): List[Coordinate] = {
    require(xValues.size == yValues.size, "Must be an x for every y.")
    (xValues, yValues).zipped.map(Coordinate)
  }

  def tuplesToLists(XY: List[Coordinate]): (List[Double], List[Double]) = {
    XY.unzip{case Coordinate(x, y) => (x, y)}
  }

  def imputeTopLeftAndBottomRight(bottomLeft: Coordinate, topRight: Coordinate): Square = {

  }

  /**
    * Generates a list of all the squares in a 2D square coordinate space
    * x and y have to be equal lengths
    * @param xValues
    * @param yValues
    * @param partitionNum number of intervals to slice each dimension into
    * @return list of squares, themselves lists, defined by the (x, y) coordinates of their vertices
    */
  def generateBottomLeftAndTopRight(xValues: List[Double], yValues: List[Double],
                      partitionNum: Int): List[Seq[(Double, Double)]] = {
    if (partitionNum == 0) throw new IllegalArgumentException("Can not partition a dimension into 0 intervals.")
    val (xStepSize, yStepSize) =
      (abs(xValues.max - xValues.min)/partitionNum,  abs(xValues.max - xValues.min)/partitionNum)
    val (xSteps, ySteps) = (xValues.min to xValues.max by xStepSize, yValues.min to yValues.max by yStepSize)
    if (!xSteps.contains(xValues.max) || !ySteps.contains(yValues.max)) throw new ArithmeticException{
      "NumericRange is not inclusive as expected due to floating point arithmetic errors. Try a different " +
      "partition number."
    }
    val (xSlide, ySlide) = (xSteps.iterator.sliding(2).toList, ySteps.iterator.sliding(2).toList)
    xSlide.flatMap(xWindow => ySlide.map(yWindow => xWindow.zip(yWindow)))
  }


}
