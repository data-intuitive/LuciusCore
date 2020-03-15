package com.dataintuitive.luciuscore.lenses

import scalaz.Lens
import com.dataintuitive.luciuscore.genes._
import com.dataintuitive.luciuscore.Model._

object CompoundAnnotationsLenses extends Serializable {

    val compoundLens = Lens.lensu[CompoundAnnotations, Compound](
        (a, value) => a.copy(compound = value),
        _.compound
    )
    val knownTargetsLens = Lens.lensu[CompoundAnnotations, Option[Seq[GeneType]]](
        (a, value) => a.copy(knownTargets = value),
        _.knownTargets
    )
    val predictedTargetLens = Lens.lensu[CompoundAnnotations, Option[Seq[GeneType]]](
        (a, value) => a.copy(predictedTargets = value),
        _.predictedTargets
    )

    val cL = compoundLens

}
