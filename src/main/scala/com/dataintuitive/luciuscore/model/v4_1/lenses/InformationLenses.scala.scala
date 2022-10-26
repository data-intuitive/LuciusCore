package com.dataintuitive.luciuscore
package model.v4_1
package lenses

import OptionLenses._

import scalaz.Lens

object InformationLenses extends Serializable {

  val processingLevelLens = Lens.lensu[Information, Option[String]](
    (a, value) => a.copy(processing_level = value.getOrElse("")),
    a => Some(a.processing_level)
  )

  val replicatesLens = Lens.lensu[Information, Option[Int]](
    (a, value) => {
      val arr = value match {
        case None => a.details
        case Some(i) =>
          // Resize array, both making longer and shorter
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

  val cellLens = Lens.lensu[Information, Seq[Option[String]]](
    (a, value) => a.copy(details = a.details.zipAll(value, new InformationDetail, None).collect{ case (a, b) => a.copy(cell = b) }),
    a => a.details.map(_.cell)
  )

  val batchLens = Lens.lensu[Information, Seq[Option[String]]](
    (a, value) => a.copy(details = a.details.zipAll(value, new InformationDetail, None).collect{ case (a, b) => a.copy(batch = b) }),
    a => a.details.map(_.batch)
  )

  val plateLens = Lens.lensu[Information, Seq[Option[String]]](
    (a, value) => a.copy(details = a.details.zipAll(value, new InformationDetail, None).collect{ case (a, b) => a.copy(plate = b) }),
    a => a.details.map(_.plate)
  )

  val wellLens = Lens.lensu[Information, Seq[Option[String]]](
    (a, value) => a.copy(details = a.details.zipAll(value, new InformationDetail, None).collect{ case (a, b) => a.copy(well = b) }),
    a => a.details.map(_.well)
  )

  val yearLens = Lens.lensu[Information, Seq[Option[String]]](
    (a, value) => a.copy(details = a.details.zipAll(value, new InformationDetail, None).collect{ case (a, b) => a.copy(year = b) }),
    a => a.details.map(_.year)
  )

  val extraLens = Lens.lensu[Information, Seq[Option[String]]](
    (a, value) => a.copy(details = a.details.zipAll(value, new InformationDetail, None).collect{ case (a, b) => a.copy(extra = b) }),
    a => a.details.map(_.extra)
  )

  val serializedCellLens =  cellLens >=> serializeStringSeqLens
  val serializedBatchLens = batchLens >=> serializeStringSeqLens
  val serializedPlateLens = plateLens >=> serializeStringSeqLens
  val serializedWellLens = wellLens >=> serializeStringSeqLens
  val serializedYearLens = yearLens >=> serializeStringSeqLens
  val serializedExtraLens = extraLens >=> serializeStringSeqLens

  val safeProcessingLevel = processingLevelLens >=> safeStringLens("No processing level info")
  val safeReplicates = replicatesLens >=> safeIntLens(0)

  val safeCellLens = serializedCellLens >=> safeStringLens("No cell info")
  val safeBatchLens = serializedBatchLens >=> safeStringLens("No batch info")
  val safePlateLens = serializedPlateLens >=> safeStringLens("No plate info")
  val safeWellLens = serializedWellLens >=> safeStringLens("No well info")
  val safeYearLens = serializedYearLens >=> safeStringLens("No year info")
  val safeExtraLens = serializedExtraLens >=> safeStringLens("No extra info")

}
