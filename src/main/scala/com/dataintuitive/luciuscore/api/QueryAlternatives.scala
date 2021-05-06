package com.dataintuitive.luciuscore.api

/**
  * An object containing Sets that correspond to alternative names
  * to refer to the same fields in the data.
  *
  * This makes the eventual API flexible in dealing with user queries.
  */
object QueryAlternatives {

  val ID = Set("id", "pwid")

  // Calculated
  val ZHANG = Set("zhang", "similarity", "Zhang", "Similarity")

  // Information
  val BATCH = Set("batch", "Batch")
  val PLATE = Set("plateid", "PlateId")
  val WELL  = Set("well", "Well")
  val CELL  = Set("protocolname", "cellline", "CellLine", "ProtocolName", "protocol", "Protocol", "cell")
  val YEAR  = Set("year", "Year")

  // Perturbation
  val P_ID = Set("jnjs", "Jnjs", "cid", "pid", "compound_id", "brdid")
  val P_SMILES = Set("Smiles", "smiles", "SMILES", "compound_smiles")
  val P_INCHIKEY = Set("inchikey", "Inchikey", "compound_inchikey")
  val P_NAME = Set("compoundname", "CompoundName", "Compoundname", "name", "Name", "compound_name")
  val P_TYPE = Set("Type", "type", "compound_type")
  val P_TARGETS = Set("targets", "knownTargets", "Targets", "compound_targets")
  val DOSE  = Set("concentration", "Concentration", "dose")
  val TIME  = Set("duration", "time", "Time", "Duration")

  // Filters
  val FILTERS = Set("filters", "Filters", "filter", "filters")

  // Derived
  val SIGNIFICANTGENES = Set("significantGenes")

}

