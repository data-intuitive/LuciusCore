package com.dataintuitive.luciuscore.lenses

import scalaz.Lens
import com.dataintuitive.luciuscore.Model._

object SampleLenses extends Serializable {

    val pwidLens = Lens.lensu[Sample, Option[String]](
        (a, value) => a.copy(pwid = value),
        _.pwid
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

}
