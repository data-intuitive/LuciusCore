package com.dataintuitive.luciuscore.io

import com.dataintuitive.luciuscore.io.ParseFunctions._
import com.dataintuitive.test.BaseSparkContextSpec
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

/**
  * Created by toni on 07/09/16.
  */
class ParseFunctionsTest extends AnyFunSpec with BaseSparkContextSpec with Matchers {

  val arr = Array(
    Array("col1", "col2", "col3"),
    Array("1",    "2",    "3"   ),
    Array("4",    "5",    "6"   )
  )
  val rdd = sc.parallelize(arr)

  describe("extractFeatures") {

    describe("Normal case") {

      it("should parse and extract the correct columns") {
        val result = Array(
          Array(Some("1"), Some("2")),
          Array(Some("4"), Some("5"))
        )
        assert(extractFeatures(rdd, Seq("col1", "col2")).collect === result)
      }

      it("should include header when asked to") {
        val result = Array(
          Array(Some("col1"), Some("col2")),
          Array(Some("1"), Some("2")),
          Array(Some("4"), Some("5"))
        )
        assert(extractFeatures(rdd, Seq("col1", "col2"), includeHeader = true).collect === result)
      }

      it("should preserve the order of features provided") {
        val result = Array(
          Array(Some("col2"), Some("col1")),
          Array(Some("2"), Some("1")),
          Array(Some("5"), Some("4"))
        )
        assert(extractFeatures(rdd, Seq("col2", "col1"), includeHeader = true).collect === result)
      }

    }

    describe("Edge cases") {

      it("should cope with 1 feature") {

        extractFeatures(rdd, Seq("col1")).collect should equal (Array(Array(Some("1")), Array(Some("4"))))

      }

      it("should cope with empty features") {

        extractFeatures(rdd, Seq()).collect should equal (Array(Array(), Array()))
        extractFeatures(rdd, Seq()).collect should have size (rdd.count - 1)
        extractFeatures(rdd, Seq(), includeHeader=true).collect should have size (rdd.count)

      }

      it("should cope with wrong feature names") {
        val result = Array(
          Array(Some("1"), None),
          Array(Some("4"), None)
        )
        extractFeatures(rdd, Seq("col1", "wrong")).collect should equal (result)
      }

    }

  }

  describe("extractFeaturesKV") {

    it("should parse and extract the correct columns") {
      val result = Array(
        (Some("1"), Array(Some("2"))),
        (Some("4"), Array(Some("5")))
      )
      extractFeaturesKV(rdd, "col1", Seq("col2")).collect.head._1 should equal (Some("1"))
      extractFeaturesKV(rdd, "col1", Seq("col2")).collect.head._2 should equal (Array(Some("2")))
    }

    it("should include header when asked to") {
      val result = Array(
        (Some("col1"), Array(Some("col2"))),
        (Some("1"), Array(Some("2"))),
        (Some("4"), Array(Some("5")))
      )
      extractFeaturesKV(rdd, "col1", Seq("col2"), includeHeader=true).collect.head._1 should equal (Some("col1"))
      extractFeaturesKV(rdd, "col1", Seq("col2"), includeHeader=true).collect.head._2 should equal (Array(Some("col2")))
    }

  }

}
