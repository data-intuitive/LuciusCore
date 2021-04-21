package com.dataintuitive.luciuscore
package model.v3
package lenses

import scalaz.Lens

object SampleLenses extends Serializable {

    val idLens = Lens.lensu[Sample, Option[String]](
        (a, value) => a.copy(id = value),
        _.id
    )
    // Backward compability
    val pwidLens = Lens.lensu[Sample, Option[String]](
        (a, value) => a.copy(id = value),
        _.id
    )
    val batchLens = Lens.lensu[Sample, Option[String]](
        (a, value) => a.copy(batch = value),
        _.batch
    )
    val plateidLens = Lens.lensu[Sample, Option[String]](
        (a, value) => a.copy(plateid = value),
        _.plateid
    )
    val wellLens = Lens.lensu[Sample, Option[String]](
        (a, value) => a.copy(well = value),
        _.well
    )
    val protocolnameLens = Lens.lensu[Sample, Option[String]](
        (a, value) => a.copy(protocolname = value),
        _.protocolname
    )
    val concentrationLens = Lens.lensu[Sample, Option[String]](
        (a, value) => a.copy(concentration = value),
        _.concentration
    )
    val yearLens = Lens.lensu[Sample, Option[String]](
        (a, value) => a.copy(year = value),
        _.year
    )
    val timeLens = Lens.lensu[Sample, Option[String]](
        (a, value) => a.copy(time = value),
        _.time
    )

}
