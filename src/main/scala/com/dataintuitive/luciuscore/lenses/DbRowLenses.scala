package com.dataintuitive.luciuscore.lenses

import scalaz.Lens
import com.dataintuitive.luciuscore.Model._

object DbRowLenses extends Serializable {

    val pwidLens = Lens.lensu[DbRow, Option[String]](
        (a, value) => a.copy(pwid = value),
        _.pwid
    )
    val sampleAnnotationsLens = Lens.lensu[DbRow, SampleAnnotations](
        (a, value) => a.copy(sampleAnnotations = value),
        _.sampleAnnotations
    )
    val compoundAnnotationsLens = Lens.lensu[DbRow, CompoundAnnotations](
        (a, value) => a.copy(compoundAnnotations = value),
        _.compoundAnnotations
    )

    // Shorthands
    val saL = sampleAnnotationsLens
    val caL = compoundAnnotationsLens

    import SampleAnnotationsLenses._
    import CompoundAnnotationsLenses._
    import OptionLenses._

    val jnjsLens = caL >=> compoundLens >=> CompoundLenses.jnjsLens
    val jnjbLens = caL >=> compoundLens >=> CompoundLenses.jnjbLens
    val smilesLens = caL >=> compoundLens >=> CompoundLenses.smilesLens
    val inchikeyLens = caL >=> compoundLens >=> CompoundLenses.inchikeyLens
    val nameLens = caL >=> compoundLens >=> CompoundLenses.nameLens
    val ctypeLens = caL >=> compoundLens >=> CompoundLenses.ctypeLens
    val knownTargetsLens = caL >=> CompoundAnnotationsLenses.knownTargetsLens
    val predictedTargetsLens = caL >=> CompoundAnnotationsLenses.predictedTargetsLens

    val batchLens = saL >=> sampleLens >=> SampleLenses.batchLens
    val plateidLens = saL >=> sampleLens >=> SampleLenses.plateidLens
    val wellLens = saL >=> sampleLens >=> SampleLenses.wellLens
    val protocolnameLens = saL >=> sampleLens >=> SampleLenses.protocolnameLens
    val concentrationLens = saL >=> sampleLens >=> SampleLenses.concentrationLens
    val yearLens = saL >=> sampleLens >=> SampleLenses.yearLens

    val safePwidLens = pwidLens >=> safeStringLens("No platewellid")

    val safeJnjsLens = jnjsLens >=> safeStringLens("No JNJs")
    val safeJnjbLens = jnjbLens >=> safeStringLens("No JNJb")
    val safeSmilesLens = smilesLens >=> safeStringLens("No smiles")
    val safeInchikeyLens = inchikeyLens >=> safeStringLens("No inchikey")
    val safeNameLens = nameLens >=> safeStringLens("No name")
    val safeCtypeLens = ctypeLens >=> safeStringLens("No ctype")
    val safeKnownTargetsLens = knownTargetsLens >=> targetsLens
    val safePredictedTargetsLens = predictedTargetsLens >=> targetsLens

    val safeBatchLens = batchLens >=> safeStringLens("No batch")
    val safePlateidLens = plateidLens >=> safeStringLens("No plateid")
    val safeWellLens = wellLens >=> safeStringLens("No well")
    val safeProtocolnameLens = protocolnameLens >=> safeStringLens("No protocol")
    val safeConcentrationLens = concentrationLens >=> safeStringLens("No concentration")
    val safeYearLens = yearLens >=> safeStringLens("No year")

}

