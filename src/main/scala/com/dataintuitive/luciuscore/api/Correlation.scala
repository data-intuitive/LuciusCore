package com.dataintuitive.luciuscore
package api

import model.v4._
import genes._
import filters._
import signatures._

import lenses.CombinedPerturbationLenses.lengthLens

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.SparkSession

import scala.collection.immutable.Map

object Correlation extends ApiFunctionTrait {

  import BinningFunctionsBis._

  case class SpecificData(
    signatureQuery1: List[String],
    signatureQuery2: List[String],
    bins: Int,
    filters: Seq[(String, Seq[String])]
  )

  type JobOutput = Any // Seq[Map[String, Any]]

  val infoMsg = s""

  val helpMsg =
    s"""
    |$infoMsg
    |""".stripMargin

  def header(data: JobData) = s""

  def result(data: JobData)(implicit sparkSession: SparkSession) = {

    import sparkSession.implicits._

    val CachedData(db, flatDb, genesDb, _) = data.cachedData
    val SpecificData(rawSignature1, rawSignature2, bins, filters) = data.specificData
    implicit val genes = genesDb

    val qfilters = filters.map{ case(key,values) => QFilter(key, values) }

    val signature1 = new SymbolSignature(rawSignature1.toArray)
    val signature2 = new SymbolSignature(rawSignature2.toArray)

    val iSignature1 = signature1.toIndexSignature
    val iSignature2 = signature2.toIndexSignature

    val vLength = lengthLens.get(db.first)

    val query1 = iSignature1.toOrderedRankVector(vLength)
    val query2 = iSignature2.toOrderedRankVector(vLength)
    val queries = List(query1, query2)

    // Add Zhang score
    // Filter as soon as possible
    val zhangScored =
      db
        .filter(row => FilterFunctions.isMatch(qfilters, row.filters))
        .flatMap { row => DbFunctions.addScores(row, queries) }

    implicit val dimension = bins
    val p1:Point = (BigDecimal(-1.0), BigDecimal(-1.0))
    val p4:Point = (BigDecimal(1.0), BigDecimal(1.0))

    val tiling = Tiling(dimension, Square(p1, p4))

    val hashedTiles = zhangScored
      .map{sp => (BigDecimal(sp.scores(0)), BigDecimal(sp.scores(1)))}
      .map(x => tiling.inTile(x))
      .map(_.hash)
      .map((_, 1))

    val reduced = hashedTiles.rdd.reduceByKey(_+_)

    val binsWithCounts = reduced
      .collect
      .map{case (tileHash, count) => (inverseHash(tileHash), count)}
      .map{case (bin, v) => Map("x_bin_start" -> bin.x_bin_start, 
                                "x_bin_end" -> bin.x_bin_end,
                                "y_bin_start" -> bin.y_bin_start, 
                                "y_bin_end" -> bin.y_bin_end, 
                                "count" -> v)}
      .toList

    binsWithCounts

  }

   // def correlation = result _

}

// TODO: Merge this with the binning utilities
object BinningFunctionsBis extends Serializable {

  type Point = Tuple2[BigDecimal,BigDecimal]
  type Tile = Tuple2[Int, Int]

  implicit class PimpedPoint(val tuple:Point) extends Serializable {
    // Transform (shift-scale) to [0,1] square
    def transform:Point = {
      ( (tuple._1 + 1.0) / 2.0, (tuple._2 + 1.0) / 2.0)
    }

    // Transform (shift-scale) back to to [-1,1] square
    def inverseTransform:Point = {
      ( tuple._1 * 2.0 - 1.0,  tuple._2 * 2.0 - 1.0 )
    }

    def x = tuple._1
    def y = tuple._2
  }

  def inverseHash(t: Int)(implicit dim:Int):Tile = {
    (t / dim, t % dim)
  }

  implicit class PimpedTile(val t:Tile) extends Serializable  {

    def hash(implicit dim:Int):Int = {
      t._2 * dim + t._1
    }

    def leftBottom(implicit partitionNum:Int):Point = {
      val x = BigDecimal(t.x) / partitionNum
      val y = BigDecimal(t.y) / partitionNum

      (x,y).inverseTransform
    }

    def rightTop(implicit partitionNum:Int):Point = {
      val x = BigDecimal(t.x) / partitionNum
      val y = BigDecimal(t.y) / partitionNum

      val deltaX = BigDecimal(1) / partitionNum
      val deltaY = BigDecimal(1) / partitionNum

      (x + deltaX, y + deltaY).inverseTransform
    }

    def x_bin_start(implicit partitionNum:Int):BigDecimal = leftBottom(partitionNum).x
    def x_bin_end(implicit partitionNum:Int):BigDecimal = rightTop(partitionNum).x
    def y_bin_start(implicit partitionNum:Int):BigDecimal = leftBottom(partitionNum).y
    def y_bin_end(implicit partitionNum:Int):BigDecimal = rightTop(partitionNum).y

    def x = t._1
    def y = t._2

  }

  case class Square(leftBottom: Point, leftTop: Point, rightBottom: Point, rightTop: Point) {
    val center = (this.leftBottom.x + (this.rightBottom.x-this.leftBottom.x)/2,
      this.leftBottom.y + (this.leftTop.y - this.leftBottom.y)/2)
    def toXY: (Vector[BigDecimal], Vector[BigDecimal]) = {
      (Vector(this.leftBottom.x, this.rightTop.x), Vector(this.leftBottom.y, this.rightTop.y))
    }
    override def toString = "[" + leftBottom + "-" + rightTop + "]"
  }


  object Square {
    def apply(leftBottom: Point, rightTop: Point):Square =
      Square(leftBottom, (leftBottom.x, rightTop.y), (rightTop.x, leftBottom.y), rightTop)
  }

  case class Tiling(partitionNum: Int, area: Square) {

    /* Be careful, the _exact_ upper bound of a bin will be added to the next bin with this */
   def inTile(p: Point):Tile = {
     val x = p.transform.x * partitionNum
     val y = p.transform.y * partitionNum
     (x.setScale(0, BigDecimal.RoundingMode.DOWN).toInt, y.setScale(0, BigDecimal.RoundingMode.DOWN).toInt)
   }

   val squares = {
     if (partitionNum <= 0) None
     else Some{
       val (xMin, xMax, yMin, yMax) =  (area.leftBottom.x, area.rightBottom.x, area.leftBottom.y, area.leftTop.y)
       val (xDiff, yDiff) = (xMax - xMin, yMax - yMin)
       val (xStepSize, yStepSize) =
         (xDiff.abs/partitionNum, yDiff.abs/partitionNum)
       val (xSteps, ySteps) = (xMin to xMax by xStepSize toList, yMin to yMax by yStepSize toList)
       val xStepsSafe = if (!xSteps.contains(xMax)) xSteps.init ::: List(xMax) else xSteps
       val yStepsSafe = if (!ySteps.contains(yMax)) ySteps.init ::: List(yMax) else ySteps
       val (xSlide, ySlide) = (xStepsSafe.iterator.sliding(2).toVector, yStepsSafe.iterator.sliding(2).toVector)

       val intervals = for {
         x <- xSlide
         y <- ySlide
       } yield (x,y)

       intervals.map{case (xInt, yInt) => Square((xInt.toList.head, yInt.toList.head), (xInt.toList.last, yInt.toList.last))}
     }
   }

  }

}
