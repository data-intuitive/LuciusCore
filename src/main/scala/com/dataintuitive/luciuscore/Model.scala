package com.dataintuitive.luciuscore

import genes._
import signatures._

/**
  * The types and classes used throughout Lucius.
  *
  * Be aware: The gene-model and signature model are not included here.
  */
object Model extends Serializable {

  // For Vector derivatives: A ValueVector is for t-stats and p-stats
  type Value = Double
  type ValueVector = Array[Value]

  // A RankVector contains ranks.
  type Rank = Double
  type RankVector = Array[Rank]

  type SignatureType = GeneVector

  type Index = Int

  type Row = Array[Option[String]]

  case class DbRow(
                    val pwid: Option[String],
                    val sampleAnnotations: SampleAnnotations,
                    val compoundAnnotations: CompoundAnnotations
                  ) extends Serializable

  case class SampleAnnotations(
                                val sample: Sample,
                                val t: Option[Array[Double]] = None,
                                val p: Option[Array[Double]] = None,
                                val r: Option[Array[Double]] = None
                              ) extends Serializable {

    def getSample = sample

    def tDefined = t.isDefined
    def pDefined = p.isDefined
    def rDefined = r.isDefined

  }

  // Moving Targets out of the way into seperate object!!!!
  case class Compound(
                       jnjs: Option[String],
                       jnjb: Option[String] = None,
                       smiles: Option[String] = None,
                       inchikey: Option[String] = None,
                       name: Option[String] = None,
                       ctype: Option[String] = None
                     ) extends Serializable {

    def getJnj = jnjs.getOrElse("Compound jnjs not available")
    def getJnjs = jnjs.getOrElse("Compound Jnjs not available")
    def getJnjb = jnjb.getOrElse("Compound Jnjb not available")
    def getSmiles = smiles.getOrElse("Compound smiles code not availalbe")
    def getInchikey = inchikey.getOrElse("Compound inchikey not available")
    def getName = name.getOrElse("Compound name not available")
    def getType = ctype.getOrElse("Compound type not availalbe")

  }

  object Compound {
    def apply(compoundString:String):Compound = Compound(Some(compoundString))
  }

  case class CompoundAnnotations(
                       val compound: Compound,
                       val knownTargets: Option[Seq[GeneType]] = None,
                       val predictedTargets: Option[Seq[GeneType]] = None
                      ) extends Serializable {

    // Convenience method: usually jnjs is used as identifier
    def jnj = compound.jnjs

    // Map None to empty set as part of the high-level API
    def getKnownTargets = knownTargets.getOrElse(Seq())
    def getPredictedTargets = predictedTargets.getOrElse(Seq())
    def knownTargetsDefined = knownTargets.isDefined
    def predictedTargetsDefined = predictedTargets.isDefined

  }

  case class Sample(
                     val pwid: Option[String],
                     val batch: Option[String] = None,
                     val plateid: Option[String] = None,
                     val well: Option[String] = None,
                     val protocolname: Option[String] = None,
                     val concentration: Option[String] = None,
                     val year: Option[String] = None
                   ) extends Serializable {

    def getPwid = pwid.getOrElse("Sample pwid not available")
    def getBatch = batch.getOrElse("Sample batch not available")
    def getPlateid = plateid.getOrElse("Sample plateid not available")
    def getWell = well.getOrElse("Sample well not available")
    def getProtocolname = protocolname.getOrElse("Sample protocolname not available")
    def getConcentration = concentration.getOrElse("Sample concentration not available")
    def getYear = year.getOrElse("Sample year not available")

  }

  object Sample {
    def apply(sampleString:String):Sample = Sample(Some(sampleString))
  }

}
