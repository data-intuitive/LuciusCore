package com.dataintuitive.luciuscore.io

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import com.dataintuitive.test.BaseSparkContextSpec
import com.dataintuitive.luciuscore.io.StatsIO._
import com.dataintuitive.luciuscore.io.SampleCompoundRelationsIO._
import com.dataintuitive.luciuscore.io.CompoundAnnotationsIO._
import com.dataintuitive.luciuscore.io.RanksIO._

/**
  * This test class tests the whole of the loading and combining functionality. It serves as an example
  * of how to work with the input files and use the API provided in LuciusCore.
  */
class IOTest extends AnyFunSpec with BaseSparkContextSpec with Matchers {

  // Load relations data from a file, here in V1 format:
  val sampleCompoundRelationsV1Source = "src/test/resources/v1/sampleCompoundRelations.txt"
  val v1Relations = loadSampleCompoundRelationsFromFileV1(sc, sampleCompoundRelationsV1Source)

  describe("sampleCompoundRelations V1") {

    it("should loadSampleCompoundRelationsFromFileV1") {
      val firstEntry = v1Relations.first
      assert(firstEntry.id.get === "BRD-A00037023")
      assert(firstEntry.compoundAnnotations.compound.id === Some("BRD-A00037023"))
      assert(firstEntry.compoundAnnotations.compound.smiles === Some("CCOC(=O)C1=C(C)N=C2SC(=CC(=O)N2C1c1ccc(OC)cc1)C(=O)OC"))
      assert(firstEntry.compoundAnnotations.compound.name === Some("BRD-A00037023"))
      assert(firstEntry.compoundAnnotations.compound.inchikey === Some("inchi-00001"))
      assert(firstEntry.compoundAnnotations.compound.ctype === Some("NA"))
      assert(firstEntry.compoundAnnotations.knownTargets === Some(Seq("PSME1")))
      assert(firstEntry.sampleAnnotations.sample.id === Some("BRD-A00037023"))
      assert(firstEntry.sampleAnnotations.sample.batch === Some("batch#"))
      assert(firstEntry.sampleAnnotations.sample.well === Some("well#"))
      assert(firstEntry.sampleAnnotations.sample.year === Some("2020"))
      assert(firstEntry.sampleAnnotations.sample.plateid === Some("plate#"))
      assert(firstEntry.sampleAnnotations.sample.concentration === Some("00.00"))
      assert(firstEntry.sampleAnnotations.sample.protocolname === Some("LINCS"))
    }

  }

  // Load relations data from a file, here in V2 format:
  val sampleCompoundRelationsV2Source = "src/test/resources/v2/sampleCompoundRelations.txt"
  val dbRelations = loadSampleCompoundRelationsFromFileV2(sc, sampleCompoundRelationsV2Source)

  describe("sampleCompoundRelations V2") {

    it("should loadSampleCompoundRelationsFromFileV2") {
      val firstEntry = dbRelations.first
      assert(firstEntry.id.get === "BRD-A00037023")
      assert(firstEntry.compoundAnnotations.compound.id === Some("BRD-A00037023"))
      assert(firstEntry.sampleAnnotations.sample.id === Some("BRD-A00037023"))
      assert(firstEntry.sampleAnnotations.sample.batch === Some("batch#"))
      assert(firstEntry.sampleAnnotations.sample.well === Some("well#"))
      assert(firstEntry.sampleAnnotations.sample.year === Some("2020"))
      assert(firstEntry.sampleAnnotations.sample.plateid === Some("plate#"))
      assert(firstEntry.sampleAnnotations.sample.concentration === Some("00.00"))
      assert(firstEntry.sampleAnnotations.sample.protocolname === Some("LINCS"))
    }

  }

  // Load compound annotations from file and add to the relations data:
  val compoundAnnotationsSource = "src/test/resources/v2/compoundAnnotations.txt"
  val annotations = loadCompoundAnnotationsFromFileV2(sc, compoundAnnotationsSource)
  val dbAnnotations = updateCompoundAnnotationsV2(annotations, dbRelations)

  describe("compoundAnnotations") {

    it("Should load the annotations file properly and update the DB") {

      val BRDA00037023 = dbAnnotations.filter(_.id.map(_.contains("BRD-A00037023")).getOrElse(false))

      assert(BRDA00037023.map(_.compoundAnnotations.compound.smiles).collect.length === 1)
      assert(BRDA00037023.map(_.compoundAnnotations.compound.smiles).collect.length === 1)
      assert(BRDA00037023.map(_.compoundAnnotations.compound.smiles).collect.head === Some("CCOC(=O)C1=C(C)N=C2SC(=CC(=O)N2C1c1ccc(OC)cc1)C(=O)OC"))
      assert(BRDA00037023.map(_.compoundAnnotations.compound.id).collect.head === Some("BRD-A00037023"))
    }

  }

  //  Uncommented, simply takes too much time:
//
//  describe("Loading stats should work including transposing") {
//
//    val tSTatsFile = "src/test/resources/tStats.txt"
//    val pSTatsFile = "src/test/resources/pStats.txt"
//
//    it("should load into array of string") {
//      val tStats = loadStatsFromFile(sc, tSTatsFile, toTranspose = true)
//      val pStats = loadStatsFromFile(sc, pSTatsFile, toTranspose = true)
//
//      val tStatsFirst = tStats.first
//      val pStatsFirst = pStats.first
//
//      assert(tStatsFirst.length === 987)
//      assert(pStatsFirst.length === 987)
//      assert(pStatsFirst.count(_ != 0.0) === 0)
//
//    }
//
//  }

  // Load statistics from file:
  val tSTatsFile = "src/test/resources/tStats-transposed-head.txt"
  val pSTatsFile = "src/test/resources/pStats-transposed-head.txt"

  val tStats = loadStatsFromFile(sc, tSTatsFile, toTranspose = false)
  val pStats = loadStatsFromFile(sc, pSTatsFile, toTranspose = false)

  describe("Loading stats should work, already in transposed form") {


    it("should load into array of string without transposing") {

      val tStatsFirst = tStats.first
      val pStatsFirst = pStats.first

      assert(tStatsFirst.length === (978 + 1))
      assert(pStatsFirst.length === (978 + 1))

    }

  }

  // Add to the relations data
  val dbTstats = updateStats(tStats, dbAnnotations, dbUpdateT)
  val db = updateStats(pStats, dbTstats, dbUpdateP)

  describe("Updating db with stats should work, includes parsing of Doubles") {

    it("should parse the results and add it to the DB") {
      // Take an entry that contains t-p vectors
      val firstEntry = db.filter(_.sampleAnnotations.p.isDefined).first
      assert(firstEntry.sampleAnnotations.p.map(_.count(_ != 0.0)) === Some(0))
    }

  }

  // Calculate Ranks
  val dbRanks = updateRanks(db)

  describe("Updating db with ranks should work") {

    it("should update the db with the correct ranks") {
      // Take an entry that contains t-p vectors
      val firstEntryA = dbRanks.filter(_.sampleAnnotations.p.isDefined).first
      assert(firstEntryA.sampleAnnotations.r.map(_.count(_ == 0.0)) === Some(0))
    }

  }


}
