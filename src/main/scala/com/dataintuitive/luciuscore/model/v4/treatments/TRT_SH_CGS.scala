package com.dataintuitive.luciuscore.model.v4.treatments

import com.dataintuitive.luciuscore.model.v4.{TRT, TRT_GENERIC}

/**
  * Treatment case class for perturbagen type trt_sh.cgs: 'Consensus signature from shRNAs targeting the same gene'
  */
case class TRT_SH_CGS(
                   id: String,
                   name: String
                 ) extends TRT(trtType = "trt_sh.cgs") with Serializable {

  type T = TRT_SH_CGS

  def toGeneric:TRT_GENERIC =
    TRT_GENERIC(
      trtType = "trt_sh.cgs",
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
  * Treatment companion object for perturbagen type trt_sh.cgs: 'Consensus signature from shRNAs targeting the same gene'
  */
object TRT_SH_CGS {

  def apply(generic: TRT_GENERIC):TRT_SH_CGS =

    TRT_SH_CGS(
      id = generic.id,
      name = generic.name
    )

}
