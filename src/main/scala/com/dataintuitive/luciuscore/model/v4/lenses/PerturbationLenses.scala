package com.dataintuitive.luciuscore
package model.v4
package lenses

import scalaz.Lens

import filters._

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

  val trtLens = Lens.lensu[Perturbation, Treatment](
    (a, value) => a.copy(trt = value),
    _.trt
  )

  val filtersLens = Lens.lensu[Perturbation, Filters](
    (a, value) => a.copy(filters = value),
    _.filters
  )

  val filtersMapLens = filtersLens >=> FilterLenses.seqFilterMapLens

}
