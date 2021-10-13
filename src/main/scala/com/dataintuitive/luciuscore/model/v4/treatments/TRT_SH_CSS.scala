package com.dataintuitive.luciuscore.model.v4.treatments

import com.dataintuitive.luciuscore.model.v4.{TRT, TRT_GENERIC}

/**
  * Treatment case class for perturbagen type trt_sh.css: 'Controls - consensus signature from shRNAs that share a common seed sequence'
  */
case class TRT_SH_CSS(
                   id: String,
                   name: String
                 ) extends TRT(trtType = "trt_sh.css") with Serializable {

  type T = TRT_SH_CSS

  def toGeneric:TRT_GENERIC =
    TRT_GENERIC(
      trtType = "trt_sh.css",
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
  * Treatment companion object for perturbagen type trt_sh.css: 'Controls - consensus signature from shRNAs that share a common seed sequence'
  */
object TRT_SH_CSS {

  def apply(generic: TRT_GENERIC):TRT_SH_CSS =

    TRT_SH_CSS(
      id = generic.id,
      name = generic.name
    )

}
