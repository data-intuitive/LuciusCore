package com.dataintuitive.luciuscore

import com.dataintuitive.luciuscore.Model.{DbRow, RankVector, SampleAnnotations}
import com.dataintuitive.luciuscore.io.SampleCompoundRelationsIO.loadSampleCompoundRelationsFromFileV2
import com.dataintuitive.test.BaseSparkContextSpec
import org.scalatest.FlatSpec
import com.dataintuitive.luciuscore.DbFunctions._

class DbFunctionsTest extends FlatSpec with BaseSparkContextSpec{

  info("Testing rank vector scoring")

  // load relations data in order to get an example DbRow object
  val sampleCompoundRelationsV2Source = "src/test/resources/v2/sampleCompoundRelations.txt"
  val aDbRow = loadSampleCompoundRelationsFromFileV2(sc, sampleCompoundRelationsV2Source).first
  // the example data does not have the r vectors we are testing, so construct new DbRow
  val newDbRow = DbRow(aDbRow.pwid,
    SampleAnnotations(aDbRow.sampleAnnotations.sample,
      Some(Array(2.0, 2.0, 2.0, 2.0)),
      Some(Array(2.0, 2.0, 2.0, 2.0)),
      Some(Array(2.0, 2.0, 2.0, 2.0))),
    aDbRow.compoundAnnotations)

  "queryDbRowPwid function" should "give a numerical value for a single query" in {
    val x: RankVector = Array.fill(4){scala.util.Random.nextInt(10).asInstanceOf[Double]}
    assert(queryDbRowPwid(newDbRow, x).values.toSeq.head.head.get.isInstanceOf[Double] === true)
  }

  it should "give a list of size two for two queries" in {
    val x: RankVector = Array.fill(4){scala.util.Random.nextInt(10).asInstanceOf[Double]}
    val y: RankVector = Array.fill(4){scala.util.Random.nextInt(10).asInstanceOf[Double]}
    assert(queryDbRowPwid(newDbRow, x, y).values.toSeq.head.size === 2)
  }

  "queryDbRow function" should "give a numerical value for a single query" in {
    val x: RankVector = Array.fill(4){scala.util.Random.nextInt(10).asInstanceOf[Double]}
    assert(queryDbRow(newDbRow, x).get._2.head.isInstanceOf[Double] === true)
  }

  it should "give a list of size two for two queries" in {
    val x: RankVector = Array.fill(4){scala.util.Random.nextInt(10).asInstanceOf[Double]}
    val y: RankVector = Array.fill(4){scala.util.Random.nextInt(10).asInstanceOf[Double]}
    assert(queryDbRow(newDbRow, x, y).get._2.size === 2)
  }

}
