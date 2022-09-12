package com.dataintuitive.luciuscore.model.v4_1.treatments

import com.dataintuitive.luciuscore.model.v4_1.{TRT, TRT_GENERIC}

/**
  * Treatment case class for perturbagen type ctl_untrt: 'Controls - Untreated cells'
  */
case class CTL_UNTRT(
                   id: String,
                   name: String
                 ) extends TRT(trtType = "ctl_untrt") with Serializable {

  type T = CTL_UNTRT

  def toGeneric:TRT_GENERIC =
    TRT_GENERIC(
      trtType = "ctl_untrt",
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
  * Treatment companion object for perturbagen type ctl_untrt: 'Controls - Untreated cells'
  */
object CTL_UNTRT {

  def apply(generic: TRT_GENERIC):CTL_UNTRT =

    CTL_UNTRT(
      id = generic.id,
      name = generic.name
    )

}
