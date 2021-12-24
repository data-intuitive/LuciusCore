package com.dataintuitive.luciuscore
package api

import scala.collection.immutable.Map

import org.apache.spark.sql.Dataset
import org.apache.spark.sql.SparkSession

import genes.GenesDB
import utilities.SignedString
import model.v4._

object CheckSignature extends ApiFunctionTrait {

  case class SpecificData(
    signature: List[String]
  )

  type JobOutput = List[Map[String, Any]]

  val infoMsg = "Returns annotations about genes (exists in l1000, symbol)."

  val helpMsg =
    s"""
    |$infoMsg
    |
    |Input:
    |- __`query`__: a gene signature where genes can be in any format symbol, ensembl, probeset, entrez (mandatory)
    """.stripMargin

  def header(data: JobData): String = s"Retrieve annotations about gene signature ${data.specificData.signature}"

  def result(data: JobData)(implicit sparkSession: SparkSession): JobOutput = {

    implicit def signString(string: String) = new SignedString(string)

    val CachedData(db, _, genesDb, _) = data.cachedData
    val SpecificData(rawSignature) = data.specificData
    implicit val genes = genesDb

    val tt = genes
      .createSymbolDictionary
      .map(_.swap)
      .flatMap{ case (gene, symbol) =>
        List(
          (gene.id, (symbol, gene.dataType)),
          (gene.ensemblid.map(_.toList.head).getOrElse("NA"), (symbol, gene.dataType)),
          (gene.symbol.map(_.toList.head).getOrElse("NA"), (symbol, gene.dataType)))
      }

    val l1000OrNot = rawSignature
      .map(gene => (gene, tt.get(gene.abs)))
      .map {
        case (gene, optionTranslation) =>
          (gene, optionTranslation.isDefined, tt.getOrElse(gene.abs, ("", "")))
      }

    l1000OrNot.map {
      case (query, found, data) =>
        Map("query" -> query, "found" -> found, "symbol" -> (query.sign + data._1), "dataType" -> data._2)
    }

  }

}
