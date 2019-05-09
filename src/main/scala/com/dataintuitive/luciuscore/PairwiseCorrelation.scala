package com.dataintuitive.luciuscore

import scala.math.{sqrt, pow}

object PairwiseCorrelation extends Serializable {



  implicit class Point[A](val tuple: (A, A))(implicit num: Numeric[A]) {
    val x: A = this.tuple._1
    val y: A = this.tuple._2
  }
  /**
  implicit def Tuple2Point[A](tuple: (A, A))(implicit evidence: A => Numeric[A]): Point[A] = {
    new Point(tuple)
  }**/

  implicit class Bin (val bin: (Int, Int)) {???}

  case class AxisAlignedSquare[A](leftBottom: Point[A], leftTop: Point[A], rightBottom: Point[A], rightTop: Point[A])
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
  }





}
