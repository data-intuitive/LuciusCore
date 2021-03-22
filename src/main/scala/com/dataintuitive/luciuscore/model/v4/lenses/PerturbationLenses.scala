package com.dataintuitive.luciuscore
package model.v4
package model.v4.lenses

import scalaz.Lens

object PerturbationLenses extends Serializable {

  val idLens = Lens.lensu[Perturbation, String](
    (a, value) => a.copy(id = value),
    _.id
  )

  val infoLens = Lens.lensu[Perturbation, Information](
    (a, value) => a.copy(info = value),
    _.info
  )

  val trtTypeLens = Lens.lensu[Perturbation, String](
    (a, value) => a.copy(trtType = value),
    _.trtType
  )

  val profilesLens = Lens.lensu[Perturbation, Profiles](
    (a, value) => a.copy(profiles = value),
    _.profiles
  )

}
