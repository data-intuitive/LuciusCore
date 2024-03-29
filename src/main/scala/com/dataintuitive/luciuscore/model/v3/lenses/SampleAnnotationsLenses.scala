package com.dataintuitive.luciuscore
package model.v3
package lenses

import scalaz.Lens

object SampleAnnotationsLenses extends Serializable {

    val sampleLens = Lens.lensu[SampleAnnotations, Sample](
        (a, value) => a.copy(sample = value),
        _.sample
    )
    val tLens = Lens.lensu[SampleAnnotations, Option[Array[Double]]](
        (a, value) => a.copy(t = value),
        _.t
    )
    val pLens = Lens.lensu[SampleAnnotations, Option[Array[Double]]](
        (a, value) => a.copy(p = value),
        _.p
    )
    val rLens = Lens.lensu[SampleAnnotations, Option[Array[Double]]](
        (a, value) => a.copy(r = value),
        _.r
    )

    val sL = sampleLens

}
