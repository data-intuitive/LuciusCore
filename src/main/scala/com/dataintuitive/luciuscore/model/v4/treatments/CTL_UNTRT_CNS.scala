package com.dataintuitive.luciuscore.model.v4.treatments

import com.dataintuitive.luciuscore.model.v4.{TRT, TRT_GENERIC}

/**
  * Treatment case class for perturbagen type ctl_untrt.cns: 'Controls - consensus signature of many untreated wells'
  */
case class CTL_UNTRT_CNS(
                      id: String,
                      name: String
                    ) extends TRT(trtType = "ctl_untrt.cns") with Serializable {

  type T = CTL_UNTRT_CNS

  def toGeneric:TRT_GENERIC =
    TRT_GENERIC(
      trtType = "ctl_untrt.cns",
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
  * Treatment companion object for perturbagen type ctl_untrt.cns: 'Controls - consensus signature of many untreated wells'
  */
object CTL_UNTRT_CNS {

  def apply(generic: TRT_GENERIC):CTL_UNTRT_CNS =

    CTL_UNTRT_CNS(
      id = generic.id,
      name = generic.name
    )

}
