package com.dataintuitive.luciuscore.lenses

import scalaz.Lens
import com.dataintuitive.luciuscore.Model._

object ScoredDbRowLenses extends Serializable {

    val scoreLens = Lens.lensu[ScoredDbRow, Double](
        (a, value) => (value, a._2),
        _._1
    )

    val DbRowLens = Lens.lensu[ScoredDbRow, DbRow](
        (a, value) => (a._1, value),
        _._2
    )

    import OptionLenses.safeStringLens

    val jnjsLens = DbRowLens >=> DbRowLenses.jnjsLens
    val jnjbLens = DbRowLens >=> DbRowLenses.jnjbLens
    val smilesLens = DbRowLens >=> DbRowLenses.smilesLens
    val inchikeyLens = DbRowLens >=> DbRowLenses.inchikeyLens
    val nameLens = DbRowLens >=> DbRowLenses.nameLens
    val ctypeLens = DbRowLens >=> DbRowLenses.ctypeLens

    val batchLens = DbRowLens >=> DbRowLenses.batchLens
    val plateidLens = DbRowLens >=> DbRowLenses.plateidLens
    val wellLens = DbRowLens >=> DbRowLenses.wellLens
    val protocolnameLens = DbRowLens >=> DbRowLenses.protocolnameLens
    val concentrationLens = DbRowLens >=>  DbRowLenses.concentrationLens
    val yearLens = DbRowLens >=> DbRowLenses.yearLens

    val safeJnjsLens = jnjsLens >=> safeStringLens("No JNJs")
    val safeJnjbLens = jnjbLens >=> safeStringLens("No JNJb")
    val safeSmilesLens = smilesLens >=> safeStringLens("No smiles")
    val safeInchikeyLens = inchikeyLens >=> safeStringLens("No inchikey")
    val safeNameLens = nameLens >=> safeStringLens("No name")
    val safeCtypeLens = ctypeLens >=> safeStringLens("No ctype")

    val safeBatchLens = batchLens >=> safeStringLens("No batch")
    val safePlateidLens = plateidLens >=> safeStringLens("No plateid")
    val safeWellLens = wellLens >=> safeStringLens("No well")
    val safeProtocolnameLens = protocolnameLens >=> safeStringLens("No protocol")
    val safeConcentrationLens = concentrationLens >=> safeStringLens("No concentration")
    val safeYearLens = yearLens >=> safeStringLens("No year")

}
