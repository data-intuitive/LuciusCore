package com.dataintuitive.luciuscore.io

import com.dataintuitive.luciuscore.model.v3.Model._
import ParseFunctions._
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import scala.language.postfixOps

/**
  * The code needed to load the data from _disk_.
  */
object SampleCompoundRelationsIO extends Serializable {

  def parseName(s: Option[String]):Option[String] = {
    val YTGCpattern = "YTGC.*".r
    s match {
      case Some("NA") => None
      case Some(YTGCpattern()) => None
      case Some("") => None
      case _ => s
    }
  }

  def parseJNJ(s: Option[String]):Option[String] = s match {
    case Some("NA") => None
    case Some("") => None
    case _ => s.map(_.split("-").head)   // Strip -AAA and other suffixes
  }


  /**
    * Function to load data in V3 format from a file. This is the format used from December 2016 onwards.
    *
    * Please note that the following modificiations or assumptions are made on ingestion:
    *  - JNJ numbers may contain suffixes (`-AAA`, `-ACT`, ...) but these are stripped on ingestion
    *  - `NA` values for JNJ number of compound names are converted to `None`
    *  - Sometimes, empty values occur as well. Turn those into `None`s.
    *
    *  There may still be names in the databases that do not correspond to real compound names,
    *  but this logic will be tackled later.
    */
  def loadSampleCompoundRelationsFromFileV3(sc: SparkContext, fileName: String): RDD[DbRow] = {

    val data = sc.textFile(fileName).map(_.split("\t"))

    val featuresToExtract = Seq("sampleID",      // 0
                                "JNJS",          // 1
                                "JNJB",          // 2
                                "Batch",         // 3
                                "plateID",       // 4
                                "well",          // 5
                                "cellLine",      // 6
                                "Concentration", // 7
                                "year",          // 8
                                "name")          // 9

    extractFeatures(data, features = featuresToExtract, includeHeader = false)
      .map { r =>
        val _id = parseJNJ(r(1))
        val _jnjb = parseJNJ(r(2))
        val _name = parseName(r(9))
        // Avoid that experiment names are used as compound name: either jnjs OR name is used
        val thisCompound = (_id, _name) match {
          case (None, _) => Compound(id = None, name = _name)
          case (_, _) => Compound(id = _id, name = None)
        }
        val thisSample = Sample(id = r(0),
                                batch = r(3),
                                plateid = r(4),
                                well = r(5),
                                protocolname = r(6),
                                concentration = r(7),
                                year = r(8))
        val thisCompoundAnnotations = CompoundAnnotations(thisCompound)
        val thisSampleAnnotations = SampleAnnotations(thisSample)
        DbRow(r(0), thisSampleAnnotations, thisCompoundAnnotations)
      }.cache
  }

  /**
    * Function to load data in V2 format from a file.
    */
  def loadSampleCompoundRelationsFromFileV2(sc: SparkContext, fileName: String): RDD[DbRow] = {

    val data = sc.textFile(fileName).map(_.split("\t"))

    val featuresToExtract = Seq("sampleID",
                                "JNJS",
                                "batchL1000",
                                "plateID",
                                "well",
                                "cellLine",
                                "Concentration",
                                "year")

    extractFeatures(data, features = featuresToExtract, includeHeader = false)
      .map { r =>
        // Strip -AAA from the compound jnj number for compatibility
        val thisCompound = Compound(id = r(1).map(_.stripSuffix("-AAA")))
        val thisSample = Sample(id = r(0),
                                batch = r(2),
                                plateid = r(3),
                                well = r(4),
                                protocolname = r(5),
                                concentration = r(6),
                                year = r(7))
        val thisCompoundAnnotations = CompoundAnnotations(thisCompound)
        val thisSampleAnnotations = SampleAnnotations(thisSample)
        DbRow(r(0), thisSampleAnnotations, thisCompoundAnnotations)
      }.cache
  }

  /**
    * Function to load data in V1 format from a file. For V1, the compound annotations are in the relations file.
    *
    * Remark: The header is important, and case-sensitive!
    */
  def loadSampleCompoundRelationsFromFileV1(sc: SparkContext, fileName: String): RDD[DbRow] = {

    val data = sc.textFile(fileName).map(_.split("\t").map(_.trim))

    val featuresToExtract = Seq("plateWellId",
                                "JNJS",
                                "JNJB",
                                "batch",
                                "plateID",
                                "well",
                                "protocolName",
                                "concentration",
                                "year",
                                "SMILES",
                                "InChIKey",
                                "compoundName",
                                "type",
                                "targets")

    extractFeatures(data, features = featuresToExtract, includeHeader = false)
      .map { r =>
        // Strip -AAA from the compound jnj number for compatibility
        val thisCompound = Compound(id = r(1).map(_.stripSuffix("-AAA")),
                                    smiles = r(9),
                                    name = r(11),
                                    inchikey = r(10),
                                    ctype = r(12))
        val thisKnownTargets = r(13).map{
           targetsString => targetsString.split(",").map(_.trim).toSet.toSeq
        }
        val thisSample = Sample(id = r(0),
                                batch = r(3),
                                plateid = r(4),
                                well = r(5),
                                protocolname = r(6),
                                concentration = r(7),
                                year = r(8))
        val thisCompoundAnnotations = CompoundAnnotations(thisCompound, knownTargets = thisKnownTargets)
        val thisSampleAnnotations = SampleAnnotations(thisSample)
        DbRow(r(0), thisSampleAnnotations, thisCompoundAnnotations)
      }.cache
  }

}
