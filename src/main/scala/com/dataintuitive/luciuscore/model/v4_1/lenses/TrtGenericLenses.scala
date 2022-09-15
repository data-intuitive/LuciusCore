package com.dataintuitive.luciuscore
package model.v4_1
package lenses

import com.dataintuitive.luciuscore.model.v4.{TRT_EMPTY, TRT_GENERIC, Treatment}
import scalaz.Lens

object TrtGenericLenses extends Serializable {

  val typeLens = Lens.lensu[TRT_GENERIC, String](
    (a, value) => a, // We should not allow modification of types in this way
    _.trtType
  )

  val idLens = Lens.lensu[TRT_GENERIC, String](
    (a, value) => a.copy(id = value),
    _.id
  )

  val nameLens = Lens.lensu[TRT_GENERIC, String](
    (a, value) => a.copy(name = value),
    _.name
  )
  val doseLens = Lens.lensu[TRT_GENERIC, Option[String]](
    (a, value) => a.copy(dose = value),
    _.dose
  )

  val doseUnitLens = Lens.lensu[TRT_GENERIC, Option[String]](
    (a, value) => a.copy(doseUnit = value),
    _.doseUnit
  )

  val timeLens = Lens.lensu[TRT_GENERIC, Option[String]](
    (a, value) => a.copy(time = value),
    _.time
  )

  val timeUnitLens = Lens.lensu[TRT_GENERIC, Option[String]](
    (a, value) => a.copy(timeUnit = value),
    _.timeUnit
  )

  val targetsLens = Lens.lensu[TRT_GENERIC, Option[List[String]]](
    (a, value) => a.copy(targets = value),
    _.targets
  )

  val inchikeyLens = Lens.lensu[TRT_GENERIC, Option[String]](
    (a, value) => a.copy(inchikey = value),
    _.inchikey
  )

  val smilesLens = Lens.lensu[TRT_GENERIC, Option[String]](
    (a, value) => a.copy(smiles = value),
    _.smiles
  )

  val pubchemIdLens = Lens.lensu[TRT_GENERIC, Option[String]](
    (a, value) => a.copy(pubchemId = value),
    _.pubchemId
  )

  val trtGenericLens = Lens.lensu[Treatment, Option[TRT_GENERIC]](
    (a, value) => a.toGeneric.copy(trt_generic = value).toSpecific,
    _.toGeneric.trt_generic
  )

  def safeTrtGenericLens = Lens.lensu[Option[TRT_GENERIC], TRT_GENERIC](
          (a, value) => Some(value),
          _.getOrElse(TRT_EMPTY)
      )
}
