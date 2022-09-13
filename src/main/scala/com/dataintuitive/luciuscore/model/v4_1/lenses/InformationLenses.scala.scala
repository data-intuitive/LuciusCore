package com.dataintuitive.luciuscore
package model.v4_1
package lenses

import OptionLenses._

import scalaz.Lens

object InformationLenses extends Serializable {

  val processingLevelLens = Lens.lensu[Information, Option[Int]](
    (a, value) => a.copy(processing_level = value.getOrElse(-1)),
    a => Some(a.processing_level)
  )

  val replicatesLens = Lens.lensu[Information, Option[Int]](
    (a, value) => {
      val arr = value match {
        case None => a.details
        case Some(i) =>
          a.details.zipAll(Seq.range(0, i), new InformationDetail, -1).filter(_._2 != -1).map(_._1)
      }
      a.copy(details = arr)
    },
    a => Some(a.details.length)
  )

  val detailsLens = Lens.lensu[Information, List[InformationDetail]](
    (a, value) => a.copy(details = value),
    _.details.toList
  )

  val cellLens = Lens.lensu[Information, Option[String]](
    (a, value) => null, //a.copy(cell = value),
    a => Some(a.details.map(_.cell).mkString(","))
  )

  val batchLens = Lens.lensu[Information, Option[String]](
    (a, value) => null, //a.copy(batch = value),
    a => Some(a.details.map(_.batch).mkString(","))
  )

  val plateLens = Lens.lensu[Information, Option[String]](
    (a, value) => null, //a.copy(plate = value),
    a => Some(a.details.map(_.plate).mkString(","))
  )

  val wellLens = Lens.lensu[Information, Option[String]](
    (a, value) => null, //a.copy(well = value),
    a => Some(a.details.map(_.well).mkString(","))
  )

  val yearLens = Lens.lensu[Information, Option[String]](
    (a, value) => null, //a.copy(year = value),
    a => Some(a.details.map(_.year).mkString(","))
  )

  val extraLens = Lens.lensu[Information, Option[String]](
    (a, value) => null, //a.copy(extra = value),
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
