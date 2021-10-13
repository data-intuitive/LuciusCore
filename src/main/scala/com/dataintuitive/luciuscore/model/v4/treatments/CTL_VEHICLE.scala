package com.dataintuitive.luciuscore.model.v4.treatments

import com.dataintuitive.luciuscore.model.v4.{TRT, TRT_GENERIC}

/**
  * Treatment case class for perturbagen type ctl_vehicle: 'Controls - vehicle for compound treatment (e.g DMSO)'
  */
case class CTL_VEHICLE(
                           name: String,
                           id: String
                         ) extends TRT(trtType = "ctl_vehicle") with Serializable {

  type T = CTL_VEHICLE

  def toGeneric:TRT_GENERIC =
    TRT_GENERIC(
      trtType = "ctl_vehicle",
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
  * Treatment companion object for perturbagen type ctl_vehicle: 'Controls - vehicle for compound treatment (e.g DMSO)'
  */
object CTL_VEHICLE {

  def apply(generic: TRT_GENERIC):CTL_VEHICLE =

    CTL_VEHICLE(
      id = generic.id,
      name = generic.name
    )

}
