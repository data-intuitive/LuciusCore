package com.dataintuitive.luciuscore
package model.v4_1
package lenses

import scalaz.Lens

object TreatmentLenses extends Serializable {

  val trtTypeLens =
    TrtGenericLenses.trtGenericLens >=>
      TrtGenericLenses.safeTrtGenericLens >=>
        TrtGenericLenses.typeLens

  val trtIdLens =
    TrtGenericLenses.trtGenericLens >=>
      TrtGenericLenses.safeTrtGenericLens >=>
        TrtGenericLenses.idLens

  val trtNameLens =
    TrtGenericLenses.trtGenericLens >=>
      TrtGenericLenses.safeTrtGenericLens >=>
        TrtGenericLenses.nameLens

  val smilesLens =
    TrtGenericLenses.trtGenericLens >=>
      TrtGenericLenses.safeTrtGenericLens >=>
        TrtGenericLenses.smilesLens

  val inchikeyLens =
    TrtGenericLenses.trtGenericLens >=>
      TrtGenericLenses.safeTrtGenericLens >=>
        TrtGenericLenses.inchikeyLens

  val pubchemIdLens =
    TrtGenericLenses.trtGenericLens >=>
      TrtGenericLenses.safeTrtGenericLens >=>
        TrtGenericLenses.pubchemIdLens

  val doseLens =
    TrtGenericLenses.trtGenericLens >=>
      TrtGenericLenses.safeTrtGenericLens >=>
        TrtGenericLenses.doseLens

  val doseUnitLens =
    TrtGenericLenses.trtGenericLens >=>
      TrtGenericLenses.safeTrtGenericLens >=>
        TrtGenericLenses.doseUnitLens

  val timeLens =
    TrtGenericLenses.trtGenericLens >=>
      TrtGenericLenses.safeTrtGenericLens >=>
        TrtGenericLenses.timeLens

  val timeUnitLens =
    TrtGenericLenses.trtGenericLens >=>
      TrtGenericLenses.safeTrtGenericLens >=>
        TrtGenericLenses.timeUnitLens

  val targetsLens =
    TrtGenericLenses.trtGenericLens >=>
      TrtGenericLenses.safeTrtGenericLens >=>
        TrtGenericLenses.targetsLens

  val safeSmilesLens = smilesLens >=> OptionLenses.stringLens
  val safeInchikeyLens = inchikeyLens >=> OptionLenses.stringLens
  val safePubchemIdLens = pubchemIdLens >=> OptionLenses.stringLens
  val safeDoseLens = doseLens >=> OptionLenses.stringLens
  val safeDoseUnitLens = doseUnitLens >=> OptionLenses.stringLens
  val safeTimeLens = timeLens >=> OptionLenses.stringLens
  val safeTimeUnitLens = timeUnitLens >=> OptionLenses.stringLens

}
