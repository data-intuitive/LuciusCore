package com.dataintuitive.luciuscore.lenses

import scalaz.Lens
import com.dataintuitive.luciuscore.genes._
import com.dataintuitive.luciuscore.Model._
import OptionLenses._
import CompoundLenses._

object CompoundAnnotationsLenses extends Serializable {

    val compoundLens = Lens.lensu[CompoundAnnotations, Compound](
        (a, value) => a.copy(compound = value),
        _.compound
    )
    val knownTargetsLens = Lens.lensu[CompoundAnnotations, Option[Seq[GeneType]]](
        (a, value) => a.copy(knownTargets = value),
        _.knownTargets
    )
    val predictedTargetsLens = Lens.lensu[CompoundAnnotations, Option[Seq[GeneType]]](
        (a, value) => a.copy(predictedTargets = value),
        _.predictedTargets
    )

    val cL = compoundLens

    val safeIdLens = compoundLens >=> idLens >=> safeStringLens("No id")
    val safeSmilesLens = compoundLens >=> smilesLens >=> safeStringLens("No smiles")
    val safeInchikeyLens = compoundLens >=> inchikeyLens >=> safeStringLens("No inchikey")
    val safeNameLens = compoundLens >=> nameLens >=> safeStringLens("No name")
    val safeCtypeLens = compoundLens >=> ctypeLens >=> safeStringLens("No ctype")
    val safeKnownTargetsLens = knownTargetsLens >=> targetsLens
    val safePredictedTargetsLens = predictedTargetsLens >=> targetsLens

    // Pending deprecation
    val safeJnjsLens = compoundLens >=> jnjsLens >=> safeStringLens("No JNJs")
    val safeJnjbLens = compoundLens >=> jnjbLens >=> safeStringLens("No JNJb")

}
