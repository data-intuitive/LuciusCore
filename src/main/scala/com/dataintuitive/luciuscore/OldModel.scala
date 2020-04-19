package com.dataintuitive.luciuscore

import genes._
import signatures._
import Model._

/**
 * The old DbRow model, in order to handle data processed earlier
 */
object OldModel extends Serializable {

  case class OldDbRow(
                    val pwid: Option[String],
                    val sampleAnnotations: OldSampleAnnotations,
                    val compoundAnnotations: OldCompoundAnnotations
                  ) extends Serializable {

      def toDbRow:DbRow = {
        val oldSample = sampleAnnotations.sample
        val newSample:Sample = Sample(
          oldSample.pwid,  // pwid renamed to id
          oldSample.batch,
          oldSample.plateid,
          oldSample.well,
          oldSample.protocolname,
          oldSample.concentration,
          oldSample.year,
          None // time not yet available in old model
          )
        val oldCompound = compoundAnnotations.compound
        val newCompound:Compound = Compound(
          oldCompound.jnjs,
          oldCompound.smiles,
          oldCompound.inchikey,
          oldCompound.name,
          oldCompound.ctype
          )
        val newSampleAnnotations:SampleAnnotations = SampleAnnotations(
            newSample,
            sampleAnnotations.t,
            sampleAnnotations.p,
            sampleAnnotations.r
          )
        val newCompoundAnnotations:CompoundAnnotations = CompoundAnnotations(
            newCompound,
            compoundAnnotations.knownTargets,
            compoundAnnotations.predictedTargets
          )
        val _filters =
          Filters() +
          Filter("raw_concentration", oldSample.concentration.getOrElse("N/A")) +
          Filter("raw_protocol", oldSample.protocolname.getOrElse("N/A")) +
          Filter("raw_type", oldCompound.ctype.getOrElse("N/A")) +
          Filter("transformed_concentration", oldSample.concentration.getOrElse("N/A")) +
          Filter("transformed_protocol", oldSample.protocolname.getOrElse("N/A")) +
          Filter("transformed_type", oldCompound.ctype.getOrElse("N/A"))
        DbRow(pwid, newSampleAnnotations, newCompoundAnnotations, filters = _filters)
      }
  }

  case class OldSampleAnnotations(
                                val sample: OldSample,
                                val t: Option[Array[Double]] = None,
                                val p: Option[Array[Double]] = None,
                                val r: Option[Array[Double]] = None
                              ) extends Serializable
  case class OldCompoundAnnotations(
                       val compound: OldCompound,
                       val knownTargets: Option[Seq[GeneType]] = None,
                       val predictedTargets: Option[Seq[GeneType]] = None
                      ) extends Serializable

  case class OldCompound(
                       jnjs: Option[String],
                       smiles: Option[String] = None,
                       inchikey: Option[String] = None,
                       name: Option[String] = None,
                       ctype: Option[String] = None
                     ) extends Serializable

  case class OldSample(
                     val pwid: Option[String],
                     val batch: Option[String] = None,
                     val plateid: Option[String] = None,
                     val well: Option[String] = None,
                     val protocolname: Option[String] = None,
                     val concentration: Option[String] = None,
                     val year: Option[String] = None
                   ) extends Serializable

}
