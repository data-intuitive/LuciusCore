package com.dataintuitive.luciuscore.model.v4_1.treatments

import com.dataintuitive.luciuscore.model.v4_1.{TRT, TRT_GENERIC}

/**
  * Treatment case class for perturbagen type trt_oe: 'cDNA for overexpression of wild-type gene'
  */
case class TRT_OE(
                   id: String,
                   name: String
                 ) extends TRT(trtType = "trt_oe") with Serializable {

  type T = TRT_OE

  def toGeneric:TRT_GENERIC =
    TRT_GENERIC(
      trtType = "trt_oe",
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
  * Treatment companion object for perturbagen type trt_oe: 'cDNA for overexpression of wild-type gene'
  */
object TRT_OE {

  def apply(generic: TRT_GENERIC):TRT_OE =

    TRT_OE(
      id = generic.id,
      name = generic.name
    )

}
