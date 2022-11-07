package com.dataintuitive.luciuscore.api.v4_1

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
  val CELL  = Set("cell", "protocolname", "cellline", "CellLine", "ProtocolName", "protocol", "Protocol")
  val YEAR  = Set("year", "Year")

  // Treatment
  val TRT       = Set("trt", "perturbagen_type", "treatment")
  val TRT_ID    = Set("trt_id", "jnjs", "Jnjs", "cid", "pid", "compound_id", "brdid")
  val TRT_NAME  = Set("trt_name", "compoundname", "CompoundName", "Compoundname", "compound_name")
  val TRT_TYPE  = Set("Type", "type", "compound_type") // DEPRECATE ME !!!
  val SMILES    = Set("smiles", "Smiles", "SMILES", "compound_smiles")
  val INCHIKEY  = Set("inchikey", "Inchikey", "compound_inchikey")
  val DOSE      = Set("dose", "concentration", "Concentration")
  val DOSE_UNIT = Set("dose_unit", "concentration_unit", "Concentration_unit")
  val TIME      = Set("time", "duration", "Time", "Duration")
  val TIME_UNIT = Set("time_unit", "duration_unit", "Time_unit", "Duration_unit")
  val TARGETS   = Set("targets", "knownTargets", "Targets", "compound_targets")

  // Filters
  val FILTERS = Set("filters", "Filters", "filter", "filters")

  // Derived
  val SIGNIFICANTGENES = Set("significantGenes")

  val PROCLEVELDETAILS     = Set("processing_level")
  val NUMREPLICATESDETAILS = Set("number_of_replicates")
  val BATCHDETAILS         = Set("batch_details")
  val PLATEDETAILS         = Set("plate_details")
  val WELLDETAILS          = Set("well_details")
  val CELLDETAILS          = Set("cell_details")
  val YEARDETAILS          = Set("year_details")
  val EXTRADETAILS         = Set("extra_details")

}
