package com.dataintuitive.luciuscore.model.v4.treatments

import com.dataintuitive.luciuscore.model.v4.{TRT, TRT_GENERIC}

/**
  * Treatment case class for perturbagen type trt_sh: 'shRNA for loss of function (LoF) of gene'
  */
case class TRT_SH(
                   id: String,
                   name: String
                 ) extends TRT(trtType = "trt_sh") with Serializable {

  type T = TRT_SH

  def toGeneric:TRT_GENERIC =
    TRT_GENERIC(
      trtType = "trt_sh",
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
  * Treatment companion object for perturbagen type trt_sh: 'shRNA for loss of function (LoF) of gene'
  */
object TRT_SH {

  def apply(generic: TRT_GENERIC):TRT_SH =

    TRT_SH(
      id = generic.id,
      name = generic.name
    )

}
