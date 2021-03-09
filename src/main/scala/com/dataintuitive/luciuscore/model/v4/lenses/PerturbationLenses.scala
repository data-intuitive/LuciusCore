package com.dataintuitive.luciuscore.model.v4.lenses

import scalaz.Lens
import com.dataintuitive.luciuscore.model.v4.Model._

object PerturbationLenses extends Serializable {

    val idLens = Lens.lensu[Perturbation, String](
        (a, value) => a.copy(id = value),
        _.id
    )

}
