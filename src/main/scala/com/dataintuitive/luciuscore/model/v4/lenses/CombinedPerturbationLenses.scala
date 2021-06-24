package com.dataintuitive.luciuscore
package model.v4
package lenses

import scalaz.Lens

object CombinedPerturbationLenses extends Serializable {

  val idLens = PerturbationLenses.idLens

  val filtersLens =
    PerturbationLenses.filtersLens

  val filtersMapLens =
    PerturbationLenses.filtersMapLens

  // Profiles
  val tLens =
    PerturbationLenses.profilesLens >=>
      DefaultProfilesLenses.profileLens >=>
        ProfileLenses.tLens

  val pLens =
    PerturbationLenses.profilesLens >=>
      DefaultProfilesLenses.profileLens >=>
        ProfileLenses.pLens

  val rLens =
    PerturbationLenses.profilesLens >=>
      DefaultProfilesLenses.profileLens >=>
        ProfileLenses.rLens

  val lengthLens =
    PerturbationLenses.profilesLens >=>
      DefaultProfilesLenses.profileLens >=>
        ProfileLenses.lengthLens

  val pTypeLens =
    PerturbationLenses.profilesLens >=>
      DefaultProfilesLenses.profileLens >=>
        ProfileLenses.pTypeLens

  val profileLens =
    PerturbationLenses.profilesLens >=>
      DefaultProfilesLenses.profileLens

  // Information Lenses
  val batchLens =
    PerturbationLenses.infoLens >=>
      InformationLenses.batchLens

  val plateLens =
    PerturbationLenses.infoLens >=>
      InformationLenses.plateLens

  val wellLens =
    PerturbationLenses.infoLens >=>
      InformationLenses.wellLens

  val cellLens =
    PerturbationLenses.infoLens >=>
      InformationLenses.cellLens

  val yearLens =
    PerturbationLenses.infoLens >=>
      InformationLenses.yearLens

  // Treatment lenses
  val pidLens =
    PerturbationLenses.trtLens >=>
      TreatmentLenses.pidLens

  val nameLens =
    PerturbationLenses.trtLens >=>
      TreatmentLenses.nameLens

  val smilesLens =
    PerturbationLenses.trtLens >=>
      TreatmentLenses.smilesLens

  val inchikeyLens =
    PerturbationLenses.trtLens >=>
      TreatmentLenses.inchikeyLens

  val pubchemIdLens =
    PerturbationLenses.trtLens >=>
      TreatmentLenses.pubchemIdLens

  val doseLens =
    PerturbationLenses.trtLens >=>
      TreatmentLenses.doseLens

  val doseUnitLens =
    PerturbationLenses.trtLens >=>
      TreatmentLenses.doseUnitLens

  val timeLens =
    PerturbationLenses.trtLens >=>
      TreatmentLenses.timeLens

  val timeUnitLens =
    PerturbationLenses.trtLens >=>
      TreatmentLenses.timeUnitLens

  val targetsLens =
    PerturbationLenses.trtLens >=>
      TreatmentLenses.targetsLens

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
