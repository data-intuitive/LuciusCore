package com.dataintuitive.luciuscore
package model.v3
package model.v3.lenses

import scalaz.Lens
import OptionLenses._

object ScoredDbRowLenses extends Serializable {

    val scoreLens = Lens.lensu[ScoredDbRow, Double](
        (a, value) => (value, a._2),
        _._1
    )

    val DbRowLens = Lens.lensu[ScoredDbRow, DbRow](
        (a, value) => (a._1, value),
        _._2
    )

    val idLens = DbRowLens >=> DbRowLenses.idLens

    val compoundIdLens = DbRowLens >=> DbRowLenses.compoundIdLens
    val smilesLens = DbRowLens >=> DbRowLenses.smilesLens
    val inchikeyLens = DbRowLens >=> DbRowLenses.inchikeyLens
    val nameLens = DbRowLens >=> DbRowLenses.nameLens
    val ctypeLens = DbRowLens >=> DbRowLenses.ctypeLens
    val knownTargetsLens = DbRowLens >=> DbRowLenses.knownTargetsLens
    val predictedTargetsLens = DbRowLens >=> DbRowLenses.predictedTargetsLens

    val batchLens = DbRowLens >=> DbRowLenses.batchLens
    val plateidLens = DbRowLens >=> DbRowLenses.plateidLens
    val wellLens = DbRowLens >=> DbRowLenses.wellLens
    val protocolnameLens = DbRowLens >=> DbRowLenses.protocolnameLens
    val concentrationLens = DbRowLens >=>  DbRowLenses.concentrationLens
    val yearLens = DbRowLens >=> DbRowLenses.yearLens
    val timeLens = DbRowLens >=> DbRowLenses.timeLens

    val filtersMapLens = DbRowLens >=> DbRowLenses.filtersMapLens
    val filtersLens = DbRowLens >=> DbRowLenses.filtersLens

    val safeIdLens = idLens >=> safeStringLens("No id")

    val safeCompoundIdLens = compoundIdLens >=> safeStringLens("No id")
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
    val safeTimeLens = timeLens >=> safeStringLens("No time")

    // Pending deprecation
    val pwidLens = DbRowLens >=> DbRowLenses.pwidLens
    val jnjsLens = DbRowLens >=> DbRowLenses.jnjsLens
    val jnjbLens = DbRowLens >=> DbRowLenses.jnjbLens

    val safePwidLens = pwidLens >=> safeStringLens("No platewellid")
    val safeJnjsLens = jnjsLens >=> safeStringLens("No JNJs")
    val safeJnjbLens = jnjbLens >=> safeStringLens("No JNJb")

}
