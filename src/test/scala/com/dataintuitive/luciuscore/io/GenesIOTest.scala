package com.dataintuitive.luciuscore.io
import com.dataintuitive.luciuscore.io.GenesIO._
import com.dataintuitive.luciuscore.genes.Genes
import com.dataintuitive.test.BaseSparkContextSpec
import org.scalatest.FlatSpec
import org.scalatest.Matchers

/**
 * Please note the location of the featureData.txt files!
 */
class GenesIOTest extends FlatSpec with BaseSparkContextSpec with Matchers {

  info("Test loading of Gene annotations from file, old format (L1000)")
  val genes = loadGenesFromFile(sc, "/Users/toni/code/compass/data/L1000/featureData.txt", "\t")

  "Loading gene data from a file in the old format" should "work" in {
    assert(genes.genes.length === 978)
    assert(genes.genes(0).symbol.get.contains("PSME1"))
  }

  info("Test loading of Gene annotations from file, new format (L22K)")
  val genesV2 = loadGenesFromFile(sc, "/Users/toni/code/compass/data/L22K/featureData.txt", "\t")

  "The new format" should "be parsed" in {
    assert(genesV2.genes.length === 22215)
  }

  it should "be parsed with the new fields" in {
    assert(genesV2.createSymbolDictionary("PSME1").dataType === "LM")
    assert(genesV2.createSymbolDictionary("PSME1").ensemblid.get === Set("ENSG00000092010"))
  }

  it should "attach an index with offset 1 to all records" in {
    genesV2.genes.head.index should equal (1)
  }

  "The Genes class" should "create an index dictionary" in {
    genesV2.createIndexDictionary(1).index should equal (1)
    genesV2.createIndexDictionary(1).symbol.get should equal (Set("PSME1"))
  }

}
