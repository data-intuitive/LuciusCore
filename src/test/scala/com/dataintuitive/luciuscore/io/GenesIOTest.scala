package com.dataintuitive.luciuscore.io

import com.dataintuitive.luciuscore.io.GenesIO._
import com.dataintuitive.luciuscore.genes.GenesDB
import com.dataintuitive.test.BaseSparkContextSpec
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.scalatest._

/**
 * Please note the location of the featureData.txt files!
 */
class GenesIOTest extends FlatSpec with BaseSparkContextSpec with Matchers {

  "Loading gene data from a file in the old format" should "work" taggedAs(IOtag) in {
    val genes = loadGenesFromFile(sc, "/Users/toni/code/compass/data/L1000/featureData.txt", "\t")
    assert(genes.length === 978)
    assert(genes(0).symbol.get.contains("PSME1"))
  }

  "The new format" should "be parsed" taggedAs(IOtag) in {
    val genesV2 = loadGenesFromFile(sc, "/Users/toni/code/compass/data/L22K/featureData.txt", "\t")
    assert(genesV2.length === 22215)
  }

}
