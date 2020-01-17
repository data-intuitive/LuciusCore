package com.dataintuitive.luciuscore.io
import com.dataintuitive.luciuscore.io.ProbesetsIO._
import com.dataintuitive.test.BaseSparkContextSpec
import org.scalatest.FlatSpec
import org.scalatest.Matchers

/**
 * Please note the location of the featureData.txt files!
 */
class ProbesetsIOTest extends FlatSpec with BaseSparkContextSpec with Matchers {

  info("Test loading of Gene annotations from file, old format (L1000)")
  val probesets = loadProbesetsFromFile(sc, "/Users/toni/code/compass/data/L22K/featureDataEnriched.tsv", "\t")

  "Loading probeset data from a file" should "work" in {
    assert(probesets.filter(_.dataType == "LM").length === 978)
    assert(probesets.sortBy(_.index).apply(0).symbol.contains("PSME1"))
  }

}
