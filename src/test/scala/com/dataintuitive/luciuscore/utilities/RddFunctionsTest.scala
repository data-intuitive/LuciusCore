package com.dataintuitive.luciuscore.lowlevel

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{FlatSpec, Matchers}
import com.dataintuitive.luciuscore.utilities.RddFunctions._
import com.dataintuitive.test.BaseSparkContextSpec

/**
  * Created by toni on 27/04/16.
  */
class RddFunctionsTest extends FlatSpec with BaseSparkContextSpec with Matchers {

  info("Test function on Vectors")

  "Simple transpose of transpose" should "return original dataset" in {
    val a:RDD[Array[String]] = sc.parallelize(
      Array(
        Array("1","2","3"),
        Array("4","5","6"),
        Array("7","8","9"))
    )
    val att:RDD[Array[String]] = transpose(transpose(a))
    assert( att.collect === a.collect )
  }

  "Transpose of non-rectangular matrix" should "return original dataset" in {
    val a:RDD[Array[String]] = sc.parallelize(
      Array(
        Array("1","2","3"),
        Array("4","5","6"))
    )
    val att:RDD[Array[String]] = transpose(transpose(a))
    assert( att.collect === a.collect )
  }

  "slices" should "be caculated correctly" in {
    slices(10, 10) should equal (Seq((0,9)))
    slices(10, 2) should equal (Seq((0,1), (2,3), (4,5), (6,7), (8,9)))
    slices(10, 3) should equal (Seq((0,2), (3,5), (6,8), (9,9)))
    slices(2, 1) should equal (Seq((0,0), (1,1)))
  }

  it should "return the same as the original transpose with batch size large" in {
    val a:RDD[Array[String]] = sc.parallelize(
      Array(
        Array("a", "1","2","3"),
        Array("b", "4","5","6"),
        Array("c", "7","8","9"))
    )
    assert(transposeInBatches(a, 4).collect === transpose(a).collect)
  }

   it should "return the same as the original transpose, batch size small" in {
    val a:RDD[Array[String]] = sc.parallelize(
      Array(
        Array("a", "1","2","3"),
        Array("b", "4","5","6"),
        Array("c", "7","8","9"))
    )
    assert(transposeInBatches(a, 2).collect === transpose(a).collect)
  }

   it should "return the same row by row" in {
    val a:RDD[Array[String]] = sc.parallelize(
      Array(
        Array("a", "1","2","3"),
        Array("b", "4","5","6"),
        Array("c", "7","8","9"))
    )
    assert(transposeInBatches(a, a.first.length).collect === transpose(a).collect)
  }
}
