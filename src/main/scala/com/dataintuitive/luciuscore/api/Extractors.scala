package com.dataintuitive.luciuscore
package api

import model.v4._
import lenses._

import QueryAlternatives._

/**
  * Convenience objects (functions) for extracting features from row in the data.
  *
  * There is one for `ScoredPerturbation` and one for `Perturbation`.
  *
  * This code depends heavily on `QueryAlternatives` and lenses.
  *
  * TODO: Avoid code duplication below.
  */
object Extractors {

  object ScoredPerturbationExtractor {

    import CombinedScoredPerturbationLenses._

    def apply(r: ScoredPerturbation, features: List[String]) = features.map {
      _ match {
        case x if ZHANG contains x         => scoreLens.get(r)
        case x if ID contains x            => idLens.get(r)
        case x if BATCH contains x         => safeBatchLens.get(r)
        case x if PLATE contains x         => safePlateLens.get(r)
        case x if WELL contains x          => safeWellLens.get(r)
        case x if CELL contains x          => safeCellLens.get(r)
        case x if DOSE contains x          => safeDoseLens.get(r)
        case x if YEAR contains x          => safeYearLens.get(r)
        case x if TIME contains x          => safeTimeLens.get(r)
        case x if TRT contains x           => trtTypeLens.get(r)
        case x if TRT_ID contains x        => trtIdLens.get(r)
        case x if TRT_NAME contains x      => trtNameLens.get(r)
        case x if SMILES contains x        => safeSmilesLens.get(r)
        case x if INCHIKEY contains x      => safeInchikeyLens.get(r)
        case x if TARGETS contains x       => targetsLens.get(r)
        case x if FILTERS contains x       => filtersMapLens.get(r).map(x => Map("key" -> x._1, "value" -> x._2)).toSeq
        // fallback
        case _                             => "Feature not found"
      }
    }

    val allFeatures = List(
      "zhang",
      "id",
      "batch",
      "plateid",
      "well",
      "cell",
      "dose",
      "year",
      "trt",
      "trt_id",
      "trt_name",
      "smiles",
      "inchikey",
      "time",
      "filters"
    )
  }

  object PerturbationExtractor {

    import CombinedPerturbationLenses._

    def apply(r: Perturbation, features: List[String], pValue:Double = 0.05) = features.map {
      _ match {
        case x if ID contains x            => idLens.get(r)
        case x if BATCH contains x         => safeBatchLens.get(r)
        case x if PLATE contains x         => safePlateLens.get(r)
        case x if WELL contains x          => safeWellLens.get(r)
        case x if CELL contains x          => safeCellLens.get(r)
        case x if DOSE contains x          => safeDoseLens.get(r)
        case x if YEAR contains x          => safeYearLens.get(r)
        case x if TIME contains x          => safeTimeLens.get(r)
        case x if TRT contains x           => trtTypeLens.get(r)
        case x if TRT_ID contains x        => trtIdLens.get(r)
        case x if TRT_NAME contains x      => trtNameLens.get(r)
        case x if SMILES contains x        => safeSmilesLens.get(r)
        case x if INCHIKEY contains x      => safeInchikeyLens.get(r)
        case x if TARGETS contains x       => targetsLens.get(r)
        case x if SIGNIFICANTGENES contains x   => pLens.get(r).map(_.count(_ <= 0.05)).getOrElse(0)
        case x if FILTERS contains x       => filtersMapLens.get(r).map(x => Map("key" -> x._1, "value" -> x._2)).toSeq
        // fallback
        case _                             => "Feature not found"
      }
    }

    val allFeatures = List(
      "id",
      "trt",
      "trt_id",
      "trt_name",
      "smiles",
      "inchikey",
      "dose",
      "time",
      "targets",
      "cell",
      "plate",
      "well",
      "significantGenes"
    )

  }

  object TreatmentExtractor {

    import TreatmentLenses._

    def apply(r:Treatment, features: List[String]) = features.map {
      _ match {
        case x if TRT contains x           => trtTypeLens.get(r)
        case x if TRT_ID contains x        => trtIdLens.get(r)
        case x if TRT_NAME contains x      => trtNameLens.get(r)
        case x if SMILES contains x        => safeSmilesLens.get(r)
        case x if INCHIKEY contains x      => safeInchikeyLens.get(r)
        case x if TARGETS contains x       => targetsLens.get(r)
        case x if DOSE contains x          => safeDoseLens.get(r)
        case x if TIME contains x          => safeTimeLens.get(r)
        // fallback
        case _                             => "Feature not found"
      }
    }

    val allFeatures = List(
      "id",
      "batch",
      "plate",
      "well",
      "cell",
      "year",
      "trt",
      "trt_id",
      "trt_name",
      "smiles",
      "inchikey",
      "dose",
      "time"
    )

  }
}
