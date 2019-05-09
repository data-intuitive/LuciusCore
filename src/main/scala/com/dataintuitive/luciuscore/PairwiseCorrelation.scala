package com.dataintuitive.luciuscore

import scala.math.{sqrt, pow, BigDecimal}

object PairwiseCorrelation extends Serializable {



  implicit class Point[A](val tuple: (A, A))(implicit num: Numeric[A]) {
    val x: A = this.tuple._1
    val y: A = this.tuple._2
  }

  case class AxisAlignedBin[A](leftBottom: Point[A], leftTop: Point[A], rightBottom: Point[A], rightTop: Point[A])
                              (implicit ordering: Ordering[A]) {
    import ordering._
    require{ leftBottom.x == leftTop.x &&
      rightBottom.x == rightTop.x &&
      leftBottom.y == rightBottom.y &&
      leftTop.y == rightTop.y} // check alignment with axes
    require{ leftBottom.x <= rightBottom.x &&
      leftTop.x <= rightTop.x &&
    leftBottom.y <= leftTop.y &&
    rightBottom.y <= rightTop.y} // check top corners greater than bottoms, rights greater than lefts

    def this(leftBottom: Point[A], rightTop: Point[A])(implicit num: Numeric[A], ordering: Ordering[A]) = this(leftBottom,
      (leftBottom.x, rightTop.y), (rightTop.x, leftBottom.y), rightTop)

    def toXY: (Vector[A], Vector[A]) = {
      (Vector(this.leftBottom.x, this.rightTop.x), Vector(this.leftBottom.y, this.rightTop.y))
    }

  }

  /**
    * see https://www.oreilly.com/library/view/algorithms-in-a/9780596516246/ch04s08.html
    * @param datum
    * @param buckets
    * @param num
    * @tparam A
    * @return
    */
  def SuperHashFunction[A](datum: Point[A], buckets: Int)(implicit num: Numeric[A]): Int = {
    import num._
    val res =
      (datum.x.toDouble * buckets.toDouble * buckets.toDouble + datum.y.toDouble) +
      (datum.x.toDouble * buckets.toDouble + datum.y.toDouble) +
      datum.y.toDouble
    res.toInt
  }

  def HashSort[A](data: Vector[(A, A)], hashFunction: ((A, A), Int) => Int): Map[Int, (A, A)] = {
    ???
  }




}
