package com.dataintuitive.luciuscore
package model.v4
package lenses

import scalaz.Lens

object CombinedPerturbationLenses extends Serializable {
    val tLens =  PerturbationLenses.profilesLens >=> DefaultProfilesLenses.profileLens >=> ProfileLenses.tLens
    val pLens =  PerturbationLenses.profilesLens >=> DefaultProfilesLenses.profileLens >=> ProfileLenses.pLens
    val rLens =  PerturbationLenses.profilesLens >=> DefaultProfilesLenses.profileLens >=> ProfileLenses.rLens
    val lengthLens = PerturbationLenses.profilesLens >=> DefaultProfilesLenses.profileLens >=> ProfileLenses.lengthLens
    val pTypeLens = PerturbationLenses.profilesLens >=> DefaultProfilesLenses.profileLens >=> ProfileLenses.pTypeLens
    val profileLens = PerturbationLenses.profilesLens >=> DefaultProfilesLenses.profileLens
}
