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

}

