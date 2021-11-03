package com.dataintuitive.luciuscore

import com.dataintuitive.luciuscore.api.Filters
import model.v4._

import org.apache.spark.sql.Dataset
import org.apache.spark.sql.SparkSession
import com.dataintuitive.test.BaseSparkContextSpec

import lenses._

trait TestData extends BaseSparkContextSpec {

  val featuresToExtract = Map(
    "pr_gene_id" -> "id",
    "pr_gene_symbol" -> "symbol", 
    "pr_is_lm" -> "dataType", 
    "pr_is_name" -> "name",
    "_" -> "na")

  val geneFile = "src/test/resources/GSE92742_Broad_LINCS_gene_info.txt"
  implicit val genesDB = new genes.GenesDB(io.GenesIO.loadGenesFromFile(sc, geneFile, "\t", featuresToExtract))
  val filters: Filters.FiltersDB = Map.empty // TODO add actual filter data, placeholder for when tests might require it

  val profileLength = genesDB.genes.length

  // Initialize the random number generator here for real unique ids
  val rnd = scala.util.Random

  def generatePerturbation(trtType: String = "trt_cp"):Perturbation = {

      val t = Some(Array.fill(profileLength)(scala.util.Random.nextGaussian))
      val p = Some(Array.fill(profileLength)(math.random))
      val profile = Profile("na", profileLength, t, p, None, None)
      val profiles = Profiles(List(profile))

      val info = Information(cell = Some("MCF7"))

      val uniqueID = "ID" + rnd.nextInt(10000).toString

      val trt = trtType match {

        case "trt_cp" => {
          val trt_cp = "CP" + scala.util.Random.nextInt(10000).toString
          TRT_GENERIC(
            trtType = "trt_cp",
            id = trt_cp,
            name = trt_cp + "-name",
            inchikey = None,
            smiles = None,
            pubchemId = None,
            dose = None,
            doseUnit = None,
            time = None,
            timeUnit = None,
            targets = None)
        }
        case "trt_lig" => {
          val trt_lig = "LIG" + scala.util.Random.nextInt(10000).toString
          TRT_GENERIC(
            trtType = "trt_lig",
            id = trt_lig,
            name = trt_lig + "-name",
            inchikey = None,
            smiles = None,
            pubchemId = None,
            dose = None,
            doseUnit = None,
            time = None,
            timeUnit = None,
            targets = None)
        }
      }

      Perturbation(uniqueID, info, profiles, trt, Nil)
  }

  val sqlContext= new org.apache.spark.sql.SQLContext(sc)
  import sqlContext.implicits._

  val testData = sc.parallelize(
    Array.fill(100)(generatePerturbation()) ++
    Array.fill(100)(generatePerturbation("trt_lig"))
  ).toDF.as[Perturbation]

  val flatData = testData.map( row =>
      api.FlatDbRow(
        row.id,
        row.info.cell.getOrElse("N/A"),
        row.trt.trt_cp.map(_.dose).getOrElse("N/A"),
        row.trtType,
        row.trt.trt.name,
        row.profiles.profile.map(_.p.map(_.count(_ <= 0.05)).getOrElse(0) > 0).getOrElse(false)
      )
    )

}
