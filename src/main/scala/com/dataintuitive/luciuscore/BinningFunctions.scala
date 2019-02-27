package com.dataintuitive.luciuscore

import scala.math.{sqrt, pow}

object BinningFunctions {

  /**
    * assumptions: We would like to reduce a scatter plot of many points to a heat map of squares, with each square
    * colored by how many points fall within it, in order to visualise very dense/large datasets.
    */

  def distance(min: Double, max: Double): Double = {
    sqrt(pow(min, 2) + pow(max, 2))
  }

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

  /**
    * Generates a list of all the smallest possible squares in a 2D square coordinate space
    * * x and y have to be equal lengths
    * @param xValues
    * @param yValues
    * @param partitionNum number of bins to partition into
    * @return list of squares defined by the (x, y) coordinates of their vertices
    */
  def generateSquares(xValues: List[Double], yValues: List[Double],
                      partitionNum: Int): List[List[(Double, Double)]] = {
    val xStepSize = distance(xValues.min, xValues.max)/partitionNum
    val yStepSize = distance(yValues.min, yValues.max)/partitionNum
    val xSteps = xValues.min until xValues.max by xStepSize
    val ySteps= yValues.min until yValues.max by yStepSize
    val xSlide = xValues.iterator.sliding(2).toList
    val ySlide = yValues.iterator.sliding(2).toList
    val squareList = xSlide.map{xTuple =>
      ySlide.flatMap{
        yTuple => xTuple.flatMap(xCoord => yTuple.map(yCoord => (xCoord, yCoord)))}
    }
    squareList
  }


}
