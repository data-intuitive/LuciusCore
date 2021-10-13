package com.dataintuitive.luciuscore.model.v4.treatments

import com.dataintuitive.luciuscore.model.v4.{TRT, TRT_GENERIC}

/**
  * Treatment case class for perturbagen type ctl_vector: 'Controls - vector for genetic perturbation (e.g empty vector, GFP)'
  */
case class CTL_VECTOR(
                       name: String,
                       id: String
                     ) extends TRT(trtType = "ctl_vector") with Serializable {

  type T = CTL_VECTOR

  def toGeneric:TRT_GENERIC =
    TRT_GENERIC(
      trtType = "ctl_vector",
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
  * Treatment companion object for perturbagen type ctl_vector: 'Controls - vector for genetic perturbation (e.g empty vector, GFP)'
  */
object CTL_VECTOR {

  def apply(generic: TRT_GENERIC):CTL_VECTOR =

    CTL_VECTOR(
      id = generic.id,
      name = generic.name
    )

}
