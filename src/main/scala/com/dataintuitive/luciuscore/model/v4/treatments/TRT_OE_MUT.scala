package com.dataintuitive.luciuscore.model.v4.treatments

import com.dataintuitive.luciuscore.model.v4.{TRT, TRT_GENERIC}

/**
  * Treatment case class for perturbagen type trt_oe.mut: 'cDNA for overexpression of mutated gene'
  */
case class TRT_OE_MUT(
                   id: String,
                   name: String
                 ) extends TRT(trtType = "trt_oe.mut") with Serializable {

  type T = TRT_OE_MUT

  def toGeneric:TRT_GENERIC =
    TRT_GENERIC(
      trtType = "trt_oe.mut",
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
  * Treatment companion object for perturbagen type trt_oe.mut: 'cDNA for overexpression of mutated gene'
  */
object TRT_OE_MUT {

  def apply(generic: TRT_GENERIC):TRT_OE_MUT =

    TRT_OE_MUT(
      id = generic.id,
      name = generic.name
    )

}
