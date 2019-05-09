package com.dataintuitive.luciuscore

import com.dataintuitive.luciuscore.PairwiseCorrelation._
import org.scalatest.FlatSpec

import scala.runtime.RichInt

class PairwiseCorrelationTest extends FlatSpec {
/**
  "Square constructor" should "correctly generate an integer square" in {
    implicit def Tuple2Point[A](tuple: (A, A))(implicit evidence: Numeric[A]): Point[A] = {
      new Point(tuple)
    }
    val square = new AxisAlignedSquare[Int]((1, 1), (1, 2), (2, 1), (2, 2))
  }
  "Square constructor" should "correctly generate an integer square" in {
    implicit def IntTuple2Point[Int](tuple: (Int, Int)): Point[Int] = {
      implicit def Numerically(i:Int): Numeric[Int] = {Numeric(Int)}
      new Point(tuple)
    }
    val square = new AxisAlignedSquare[Int]((1, 1), (1, 2), (2, 1), (2, 2))
  }


  "Square constructor" should "correctly generate an integer square" in {
    val square = new AxisAlignedSquare[Int](Point[Int](1, 1), Point[Int](1, 2), Point[Int](2, 1), Point[Int](2, 2))
  }


  "Square constructor" should "correctly generate an integer square from just two elements" in {
    new AxisAlignedSquare[Int]((1, 1), (2, 2))
  }

  "Square constructor" should "correctly generate an integer square from just two elements" in {
    AxisAlignedSquare((1, 1), (2, 2))
  }**/

  "Square constructor" should "correctly generate an integer square" in {
    val square = new AxisAlignedBin[Int]((1, 1), (1, 2), (2, 1), (2, 2))
    assert(square.leftBottom.tuple == (1, 1) && square.leftTop.tuple == (1, 2)
      && square.rightBottom.tuple == (2, 1) & square.rightTop.tuple == (2, 2))
  }

  "Square constructor" should "correctly generate an integer square from only a partial constructor" in {
    val square = new AxisAlignedBin[Int]((1, 1), (2, 2))
    assert(square.leftBottom.tuple == (1, 1) && square.leftTop.tuple == (1, 2)
      && square.rightBottom.tuple == (2, 1) & square.rightTop.tuple == (2, 2))
  }

  "Square constructor" should "correctly generate a Double square" in {
    val square = new AxisAlignedBin[Double]((1.0, 1.0), (1.0, 2.0), (2.0, 1.0), (2.0, 2.0))
    assert(square.leftBottom.tuple == (1.0, 1.0) && square.leftTop.tuple == (1.0, 2.0)
      && square.rightBottom.tuple == (2.0, 1.0) & square.rightTop.tuple == (2.0, 2.0))
  }

  "SuperHashFunction" should "return some output" in {
    val point1 = new Point[Double]((0.5, 0.6))
    val res = SuperHashFunction(point1, 5)
    printf(res.toString)
  }

  "SuperHashFunction" should "return some other output" in {
    val point1 = new Point[Int]((1, 2))
    val res = SuperHashFunction(point1, 5)
    printf(res.toString)
  }

}
