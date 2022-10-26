package com.dataintuitive.luciuscore
package model.v4_1
package lenses

import scalaz.Lens

object ScoredPerturbationLenses extends Serializable {

  val scoresLens = Lens.lensu[ScoredPerturbation, List[Double]](
    (a, value) => a.copy(scores = value),
    _.scores
  )

  val perturbationLens = Lens.lensu[ScoredPerturbation, Perturbation](
    (a, value) => a.copy(perturbation = value),
    _.perturbation
  )

}
