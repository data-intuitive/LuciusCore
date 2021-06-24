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
        case x if P_ID contains x          => pidLens.get(r)
        case x if P_SMILES contains x      => safeSmilesLens.get(r)
        case x if P_INCHIKEY contains x    => safeInchikeyLens.get(r)
        case x if P_NAME contains x        => nameLens.get(r)
        // case x if COMPOUND_TYPE contains x      => safeCtypeLens.get(r)
        case x if P_TARGETS contains x     => targetsLens.get(r)
        case x if FILTERS contains x       => filtersMapLens.get(r).map(x => Map("key" -> x._1, "value" -> x._2)).toSeq
        // fallback
        case _                             => "Feature not found"
      }
    }

    val allFeatures = List(
      "zhang",
      "id",
      "batch",
      "plateid",        // Should be plate
      "well",
      "protocolname",   // Should be cell
      "concentration",  // Should be dose
      "year",
      "time",
      "compound_id",    // Should become something else!
      "smiles",
      "inchikey",
      "name",
      "filters"
    )
  }

  object PerturbationExtractor {

    import CombinedPerturbationLenses._

    def apply(r: Perturbation, features: List[String]) = features.map {
      _ match {
        case x if ID contains x            => idLens.get(r)
        case x if BATCH contains x         => safeBatchLens.get(r)
        case x if PLATE contains x         => safePlateLens.get(r)
        case x if WELL contains x          => safeWellLens.get(r)
        case x if CELL contains x          => safeCellLens.get(r)
        case x if DOSE contains x          => safeDoseLens.get(r)
        case x if YEAR contains x          => safeYearLens.get(r)
        case x if TIME contains x          => safeTimeLens.get(r)
        case x if P_ID contains x          => pidLens.get(r)
        case x if P_SMILES contains x      => safeSmilesLens.get(r)
        case x if P_INCHIKEY contains x    => safeInchikeyLens.get(r)
        case x if P_NAME contains x        => nameLens.get(r)
        // case x if COMPOUND_TYPE contains x      => safeCtypeLens.get(r)
        case x if P_TARGETS contains x     => targetsLens.get(r)
        case x if FILTERS contains x       => filtersMapLens.get(r).map(x => Map("key" -> x._1, "value" -> x._2)).toSeq
        // fallback
        case _                             => "Feature not found"
      }
    }

    val allFeatures = List(
      "id",
      "compound_id",    // Should become something else!
      "smiles",
      "inchikey",
      "concentration",  // Should be dose
      "time",
      "compound_name",  // Should be replaced by name?
      "targets"
    )

  }

  object TreatmentExtractor {

    import TreatmentLenses._

    def apply(r:Treatment, features: List[String]) = features.map {
      _ match {
        case x if DOSE contains x          => safeDoseLens.get(r)
        case x if TIME contains x          => safeTimeLens.get(r)
        case x if P_ID contains x          => pidLens.get(r)
        case x if P_SMILES contains x      => safeSmilesLens.get(r)
        case x if P_INCHIKEY contains x    => safeInchikeyLens.get(r)
        case x if P_NAME contains x        => nameLens.get(r)
        // case x if COMPOUND_TYPE contains x      => safeCtypeLens.get(r)
        case x if P_TARGETS contains x     => targetsLens.get(r)
        // fallback
        case _                             => "Feature not found"
      }
    }

    val allFeatures = List(
      "id",
      "batch",
      "plateid",        // Should be plate
      "well",
      "protocolname",   // Should be cell
      "concentration",  // Should be dose
      "year",
      "time",
      "compound_id",    // Should be pid
      "smiles",
      "inchikey",
      "name"
    )

  }
}
