package com.dataintuitive.luciuscore
package api

import scala.collection.immutable.Map

import org.apache.spark.sql.Dataset
import org.apache.spark.sql.SparkSession

import genes.GenesDB
import utilities.SignedString
import model.v4._

object CheckSignature extends ApiFunctionTrait {

  case class JobData(db: Dataset[Perturbation], genesDB: GenesDB, version: String, signature: List[String])
  type JobOutput = List[Map[String, Any]]

  val infoMsg = "Returns annotations about genes (exists in l1000, symbol)."

  val helpMsg =
    s"""
    |$infoMsg
    |
    |Input:
    |- __`query`__: a gene signature where genes can be in any format symbol, ensembl, probeset, entrez (mandatory)
    """.stripMargin

  def result(data: JobData)(implicit sparkSession: SparkSession): JobOutput = {

    implicit def signString(string: String) = new SignedString(string)

    val JobData(db, genesDB, version, rawSignature) = data
    implicit val genes = genesDB

    val tt = genesDB
      .createSymbolDictionary
      .map(_.swap)
      .flatMap{ case (gene, symbol) =>
        List(
          (gene.id, symbol),
          (gene.ensemblid.map(_.toList.head).getOrElse("NA"), symbol),
          (gene.symbol.map(_.toList.head).getOrElse("NA"), symbol))
      }

    val l1000OrNot = rawSignature
      .map(gene => (gene, tt.get(gene.abs)))
      .map {
        case (gene, optionTranslation) =>
          (gene, optionTranslation.isDefined, tt.getOrElse(gene.abs, ""))
      }

    l1000OrNot.map {
      case (query, inL1000, symbol) =>
        Map("query" -> query, "inL1000" -> inL1000, "symbol" -> (query.sign + symbol))
    }

  }

}
