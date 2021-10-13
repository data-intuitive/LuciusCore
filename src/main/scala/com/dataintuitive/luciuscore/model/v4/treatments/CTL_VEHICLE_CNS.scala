package com.dataintuitive.luciuscore.model.v4.treatments

import com.dataintuitive.luciuscore.model.v4.{TRT, TRT_GENERIC}


/**
  * Treatment case class for perturbagen type ctl_vehicle.cns: 'Controls - consensus signature of vehicles'
  */
case class CTL_VEHICLE_CNS(
                        name: String,
                        id: String
                      ) extends TRT(trtType = "ctl_vehicle.cns") with Serializable {

  type T = CTL_VEHICLE_CNS

  def toGeneric:TRT_GENERIC =
    TRT_GENERIC(
      trtType = "ctl_vehicle.cns",
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
  * Treatment companion object for perturbagen type ctl_vehicle.cns: 'Controls - consensus signature of vehicles'
  */
object CTL_VEHICLE_CNS {

  def apply(generic: TRT_GENERIC):CTL_VEHICLE_CNS =

    CTL_VEHICLE_CNS(
      id = generic.id,
      name = generic.name
    )

}
