package com.dataintuitive.luciuscore.model.v4_1.treatments

import com.dataintuitive.luciuscore.model.v4_1.{TRT, TRT_GENERIC}

/**
  * Treatment case class for perturbagen type ctl_vector.cns: 'Controls - consensus signature of vectors'
  */
case class CTL_VECTOR_CNS(
                       id: String,
                       name: String
                     ) extends TRT(trtType = "ctl_vector.cns") with Serializable {

  type T = CTL_VECTOR_CNS

  def toGeneric:TRT_GENERIC =
    TRT_GENERIC(
      trtType = "ctl_vector.cns",
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
  * Treatment companion object for perturbagen type ctl_vector.cns: 'Controls - consensus signature of vectors'
  */
object CTL_VECTOR_CNS {

  def apply(generic: TRT_GENERIC):CTL_VECTOR_CNS =

    CTL_VECTOR_CNS(
      id = generic.id,
      name = generic.name
    )

}
