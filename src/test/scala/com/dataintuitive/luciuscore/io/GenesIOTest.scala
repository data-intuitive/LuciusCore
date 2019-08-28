package com.dataintuitive.luciuscore.io

import com.dataintuitive.luciuscore.io.GenesIO._
import com.dataintuitive.luciuscore.genes.Genes
import com.dataintuitive.test.BaseSparkContextSpec
import org.scalatest.FlatSpec
import org.scalatest.Matchers

/**
  * Created by toni on 22/04/16.
  */
class GenesIOTest extends FlatSpec with BaseSparkContextSpec with Matchers {

  info("Test loading of Gene annotations from file, old format (L1000)")

  // The old input file for gene annotations, for backward compatibility
  val genes = loadGenesFromFile(sc, "src/test/resources/geneAnnotations.txt", "\t")

  "Loading gene data from a file" should "work" in {
    assert(genes.genes(0).symbol.get === "PSME1")
  }

  info("Test loading of Gene annotations with wrong number of features")

  def genesWithWrongFeatures = loadGenesFromFile(sc,
                                "src/test/resources/geneAnnotations.txt",
                                delimiter = "\t")

  info("Test loading of Gene annotations with missing values")

  // Please note that we imitate missing values by selecting a non-existing column from the file
  val genesWithMissingFeatures = loadGenesFromFile(sc,
                                      "src/test/resources/geneAnnotations.txt",
                                      delimiter = "\t")

  "Loading gene data from a file with missing data" should "work and convert to NA" in {
    assert(genesWithMissingFeatures.genes(0).ensemblid.get === "NA")
  }

  info("Test loading of Gene annotations from file, new format (L22K)")

  // The old input file for gene annotations, for backward compatibility
  // The function loading the input distinguishes between both
  val genesV2 = loadGenesFromFile(sc, "src/test/resources/geneAnnotationsL22K.txt", "\t")

  "Additional field" should "be parsed" in {
    assert(genes.createSymbolDictionary("PSME1").dataType === "LM")
  }

}

