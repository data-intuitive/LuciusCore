package com.dataintuitive.luciuscore.model.v4.treatments

import com.dataintuitive.luciuscore.model.v4.{TRT, TRT_GENERIC}

/**
  * Treatment case class for perturbagen type trt_xpr: 'CRISPR for LLoF'
  */
case class TRT_XPR(
                   id: String,
                   name: String
                 ) extends TRT(trtType = "trt_xpr") with Serializable {

  type T = TRT_XPR

  def toGeneric:TRT_GENERIC =
    TRT_GENERIC(
      trtType = "trt_xpr",
      id = id,
      name = name,
      inchikey = None,
      smiles = None,
      pubchemId = None,
      dose = None,
      doseUnit = None,
      time = None,
      timeUnit = None,
      targets = None
    )

}

/**
  * Treatment companion object for perturbagen type trt_xpr: 'CRISPR for LLoF'
  */
object TRT_XPR {

  def apply(generic: TRT_GENERIC):TRT_XPR =

    TRT_XPR(
      id = generic.id,
      name = generic.name
    )

}
