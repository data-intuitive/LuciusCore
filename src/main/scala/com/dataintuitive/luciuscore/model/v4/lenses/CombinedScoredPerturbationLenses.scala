package com.dataintuitive.luciuscore
package model.v4
package lenses

import scalaz.Lens

object CombinedScoredPerturbationLenses extends Serializable {

  val scoreLens =
    ScoredPerturbationLenses.scoresLens

  val idLens =
    ScoredPerturbationLenses.perturbationLens >=>
      PerturbationLenses.idLens

  val filtersLens = ScoredPerturbationLenses.perturbationLens >=>
    PerturbationLenses.filtersLens

  val filtersMapLens = ScoredPerturbationLenses.perturbationLens >=>
    PerturbationLenses.filtersMapLens

  // Information Lenses
  val batchLens =
    ScoredPerturbationLenses.perturbationLens >=>
      CombinedPerturbationLenses.batchLens

  val plateLens =
    ScoredPerturbationLenses.perturbationLens >=>
      CombinedPerturbationLenses.plateLens

  val wellLens =
    ScoredPerturbationLenses.perturbationLens >=>
      CombinedPerturbationLenses.wellLens

  val cellLens =
    ScoredPerturbationLenses.perturbationLens >=>
      CombinedPerturbationLenses.cellLens

  val yearLens =
    ScoredPerturbationLenses.perturbationLens >=>
      CombinedPerturbationLenses.yearLens

  // Profiles lenses
  val tLens =
    ScoredPerturbationLenses.perturbationLens >=>
      CombinedPerturbationLenses.tLens

  val pLens =
    ScoredPerturbationLenses.perturbationLens >=>
      CombinedPerturbationLenses.pLens

  val rLens =
    ScoredPerturbationLenses.perturbationLens >=>
      CombinedPerturbationLenses.rLens

  val lengthLens =
    ScoredPerturbationLenses.perturbationLens >=>
      CombinedPerturbationLenses.lengthLens

  val pTypeLens =
    ScoredPerturbationLenses.perturbationLens >=>
      CombinedPerturbationLenses.pTypeLens

  val profileLens =
    ScoredPerturbationLenses.perturbationLens >=>
      CombinedPerturbationLenses.profileLens

  // Treatment lenses
  val pidLens =
    ScoredPerturbationLenses.perturbationLens >=>
      CombinedPerturbationLenses.pidLens

  val nameLens =
    ScoredPerturbationLenses.perturbationLens >=>
      CombinedPerturbationLenses.nameLens

  val smilesLens =
    ScoredPerturbationLenses.perturbationLens >=>
      CombinedPerturbationLenses.smilesLens

  val inchikeyLens =
    ScoredPerturbationLenses.perturbationLens >=>
      CombinedPerturbationLenses.inchikeyLens

  val pubchemIdLens =
    ScoredPerturbationLenses.perturbationLens >=>
      CombinedPerturbationLenses.pubchemIdLens

  val doseLens =
    ScoredPerturbationLenses.perturbationLens >=>
      CombinedPerturbationLenses.doseLens

  val doseUnitLens =
    ScoredPerturbationLenses.perturbationLens >=>
      CombinedPerturbationLenses.doseUnitLens

  val timeLens =
    ScoredPerturbationLenses.perturbationLens >=>
      CombinedPerturbationLenses.timeLens

  val timeUnitLens =
    ScoredPerturbationLenses.perturbationLens >=>
      CombinedPerturbationLenses.timeUnitLens

  val targetsLens =
    ScoredPerturbationLenses.perturbationLens >=>
      CombinedPerturbationLenses.targetsLens

  // Safe lenses
  val safeBatchLens = batchLens >=> OptionLenses.stringLens
  val safePlateLens = plateLens >=> OptionLenses.stringLens
  val safeWellLens = wellLens >=> OptionLenses.stringLens
  val safeCellLens = cellLens >=> OptionLenses.stringLens
  val safeYearLens = yearLens >=> OptionLenses.stringLens
  val safeSmilesLens = smilesLens >=> OptionLenses.stringLens
  val safeInchikeyLens = inchikeyLens >=> OptionLenses.stringLens
  val safePubchemIdLens = pubchemIdLens >=> OptionLenses.stringLens
  val safeDoseLens = doseLens >=> OptionLenses.stringLens
  val safeDoseUnitLens = doseUnitLens >=> OptionLenses.stringLens
  val safeTimeLens = timeLens >=> OptionLenses.stringLens
  val safeTimeUnitLens = timeUnitLens >=> OptionLenses.stringLens
  val safeTargetsLens = targetsLens >=> OptionLenses.listLens

}
