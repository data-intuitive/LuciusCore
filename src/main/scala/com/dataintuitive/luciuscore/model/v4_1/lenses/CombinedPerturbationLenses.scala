package com.dataintuitive.luciuscore
package model.v4_1
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
  val processingLevelLens =
    PerturbationLenses.infoLens >=>
      InformationLenses.processingLevelLens
  
  val replicatesLens =
    PerturbationLenses.infoLens >=>
      InformationLenses.replicatesLens

  val batchLens =
    PerturbationLenses.infoLens >=>
      InformationLenses.serializedBatchLens

  val plateLens =
    PerturbationLenses.infoLens >=>
      InformationLenses.serializedPlateLens

  val wellLens =
    PerturbationLenses.infoLens >=>
      InformationLenses.serializedWellLens

  val cellLens =
    PerturbationLenses.infoLens >=>
      InformationLenses.serializedCellLens

  val yearLens =
    PerturbationLenses.infoLens >=>
      InformationLenses.serializedYearLens

  val extraLens =
    PerturbationLenses.infoLens >=>
      InformationLenses.serializedExtraLens

  val batchDetailsLens =
    PerturbationLenses.infoLens >=>
      InformationLenses.batchLens

  val plateDetailsLens =
    PerturbationLenses.infoLens >=>
      InformationLenses.plateLens

  val wellDetailsLens =
    PerturbationLenses.infoLens >=>
      InformationLenses.wellLens

  val cellDetailsLens =
    PerturbationLenses.infoLens >=>
      InformationLenses.cellLens

  val yearDetailsLens =
    PerturbationLenses.infoLens >=>
      InformationLenses.yearLens

  val extraDetailsLens =
    PerturbationLenses.infoLens >=>
      InformationLenses.extraLens

  // Treatment lenses
  val trtTypeLens =
    PerturbationLenses.trtLens >=>
      TreatmentLenses.trtTypeLens

  val trtIdLens =
    PerturbationLenses.trtLens >=>
      TreatmentLenses.trtIdLens

  val trtNameLens =
    PerturbationLenses.trtLens >=>
      TreatmentLenses.trtNameLens

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
  val safeProcessingLevelLens = processingLevelLens >=> OptionLenses.stringLens
  val safeReplicatesLens = replicatesLens >=> OptionLenses.safeIntLens()
  val safeBatchLens = batchLens >=> OptionLenses.stringLens
  val safePlateLens = plateLens >=> OptionLenses.stringLens
  val safeWellLens = wellLens >=> OptionLenses.stringLens
  val safeCellLens = cellLens >=> OptionLenses.stringLens
  val safeYearLens = yearLens >=> OptionLenses.stringLens
  val safeExtraLens = extraLens >=> OptionLenses.stringLens

  val safeBatchDetailsLens = batchDetailsLens >=> OptionLenses.safeSeqStringLens
  val safePlateDetailsLens = plateDetailsLens >=> OptionLenses.safeSeqStringLens
  val safeWellDetailsLens = wellDetailsLens >=> OptionLenses.safeSeqStringLens
  val safeCellDetailsLens = cellDetailsLens >=> OptionLenses.safeSeqStringLens
  val safeYearDetailsLens = yearDetailsLens >=> OptionLenses.safeSeqStringLens
  val safeExtraDetailsLens = batchDetailsLens >=> OptionLenses.safeSeqStringLens

  val safeSmilesLens = smilesLens >=> OptionLenses.stringLens
  val safeInchikeyLens = inchikeyLens >=> OptionLenses.stringLens
  val safePubchemIdLens = pubchemIdLens >=> OptionLenses.stringLens
  val safeDoseLens = doseLens >=> OptionLenses.stringLens
  val safeDoseUnitLens = doseUnitLens >=> OptionLenses.stringLens
  val safeTimeLens = timeLens >=> OptionLenses.stringLens
  val safeTimeUnitLens = timeUnitLens >=> OptionLenses.stringLens
  val safeTargetsLens = targetsLens >=> OptionLenses.listLens

}
