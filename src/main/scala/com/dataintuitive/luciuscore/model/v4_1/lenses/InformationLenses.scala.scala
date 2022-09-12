package com.dataintuitive.luciuscore
package model.v4_1
package lenses

import OptionLenses._
// import Model._

import scalaz.Lens

object InformationLenses extends Serializable {

  val processingLevelLens = Lens.lensu[Information, Option[Int]](
    (a, value) => a.copy(processing_level = value.getOrElse(-1)),
    a => Some(a.processing_level)
  )

  val replicatesLens = Lens.lensu[Information, Option[Int]](
    null,
    a => Some(a.details.length)
  )

  val detailsLens = Lens.lensu[Information, List[InformationDetail]](
    (a, value) => a.copy(details = value),
    _.details.toList
  )

  val cellLens = Lens.lensu[Information, Option[String]](
    //(a, value) => a.copy(cell = value),
    null,
    a => Some(a.details.map(_.cell).mkString(","))
  )

  val batchLens = Lens.lensu[Information, Option[String]](
//    (a, value) => a.copy(batch = value),
    null,
    a => Some(a.details.map(_.batch).mkString(","))
  )

  val plateLens = Lens.lensu[Information, Option[String]](
//    (a, value) => a.copy(plate = value),
    null,
    a => Some(a.details.map(_.plate).mkString(","))
  )

  val wellLens = Lens.lensu[Information, Option[String]](
//    (a, value) => a.copy(well = value),
    null,
    a => Some(a.details.map(_.well).mkString(","))
  )

  val yearLens = Lens.lensu[Information, Option[String]](
//    (a, value) => a.copy(year = value),
    null,
    a => Some(a.details.map(_.year).mkString(","))
  )

  val extraLens = Lens.lensu[Information, Option[String]](
//    (a, value) => a.copy(extra = value),
    null,
    a => Some(a.details.map(_.extra).mkString(","))
  )

  val safeProcessingLevel = processingLevelLens >=> safeIntLens(-1)
  val safeReplicates = replicatesLens >=> safeIntLens(0)

  val safeCellLens = cellLens >=> safeStringLens("No cell info")
  val safeBatchLens = batchLens >=> safeStringLens("No batch info")
  val safePlateLens = plateLens >=> safeStringLens("No plate info")
  val safeWellLens = wellLens >=> safeStringLens("No well info")
  val safeYearLens = yearLens >=> safeStringLens("No year info")
  val safeExtraLens = extraLens >=> safeStringLens("No extra info")

}
