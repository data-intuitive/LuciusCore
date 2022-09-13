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

  val serializedCellLens = Lens.lensu[Information, Option[String]](
    (a, value) =>
      if (value.isEmpty) {
        a.copy(details = a.details.map(d => d.copy(cell = None)))
      }
      else {
        val newData = value.get.split(',')
        val newDetails = a.details.zipWithIndex.collect({ case (a, b) => a.copy(cell = newData.lift(b)) })
        a.copy(details = newDetails)
      }
    ,
    a => Some(a.details.map(_.cell).mkString(","))
  )

  val serializedBatchLens = Lens.lensu[Information, Option[String]](
    (a, value) => null, //a.copy(batch = value),
    a => Some(a.details.map(_.batch).mkString(","))
  )

  val serializedPlateLens = Lens.lensu[Information, Option[String]](
    (a, value) => null, //a.copy(plate = value),
    a => Some(a.details.map(_.plate).mkString(","))
  )

  val serializedWellLens = Lens.lensu[Information, Option[String]](
    (a, value) => null, //a.copy(well = value),
    a => Some(a.details.map(_.well).mkString(","))
  )

  val serializedYearLens = Lens.lensu[Information, Option[String]](
    (a, value) => null, //a.copy(year = value),
    a => Some(a.details.map(_.year).mkString(","))
  )

  val serializedExtraLens = Lens.lensu[Information, Option[String]](
    (a, value) => null, //a.copy(extra = value),
    a => Some(a.details.map(_.extra).mkString(","))
  )

  val safeProcessingLevel = processingLevelLens >=> safeIntLens(-1)
  val safeReplicates = replicatesLens >=> safeIntLens(0)

  val safeCellLens = serializedCellLens >=> safeStringLens("No cell info")
  val safeBatchLens = serializedBatchLens >=> safeStringLens("No batch info")
  val safePlateLens = serializedPlateLens >=> safeStringLens("No plate info")
  val safeWellLens = serializedWellLens >=> safeStringLens("No well info")
  val safeYearLens = serializedYearLens >=> safeStringLens("No year info")
  val safeExtraLens = serializedExtraLens >=> safeStringLens("No extra info")

}
