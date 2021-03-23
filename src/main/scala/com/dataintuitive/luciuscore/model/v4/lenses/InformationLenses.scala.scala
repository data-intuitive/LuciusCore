package com.dataintuitive.luciuscore
package model.v4
package lenses

import OptionLenses._

import scalaz.Lens

object InformationLenses extends Serializable {

  val cellLens = Lens.lensu[Information, Option[String]](
    (a, value) => a.copy(cell = value),
    _.cell
  )

  val batchLens = Lens.lensu[Information, Option[String]](
    (a, value) => a.copy(batch = value),
    _.batch
  )

  val plateLens = Lens.lensu[Information, Option[String]](
    (a, value) => a.copy(plate = value),
    _.plate
  )

  val wellLens = Lens.lensu[Information, Option[String]](
    (a, value) => a.copy(well = value),
    _.well
  )

  val yearLens = Lens.lensu[Information, Option[String]](
    (a, value) => a.copy(year = value),
    _.year
  )

  val extraLens = Lens.lensu[Information, Option[String]](
    (a, value) => a.copy(extra = value),
    _.extra
  )

  val safeCellLens = cellLens >=> safeStringLens("No cell info")
  val safeBatchLens = batchLens >=> safeStringLens("No batch info")
  val safePlateLens = plateLens >=> safeStringLens("No plate info")
  val safeWellLens = wellLens >=> safeStringLens("No well info")
  val safeYearLens = yearLens >=> safeStringLens("No year info")
  val safeExtraLens = extraLens >=> safeStringLens("No extra info")

}
