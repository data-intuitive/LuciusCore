package com.dataintuitive.luciuscore
package model.v4
package model.v4.lenses

import scalaz.Lens

object PerturbationLenses extends Serializable {

    val idLens = Lens.lensu[Perturbation, String](
        (a, value) => a.copy(id = value),
        _.id
    )

}
