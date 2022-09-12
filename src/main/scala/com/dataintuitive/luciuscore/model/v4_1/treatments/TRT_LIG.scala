package com.dataintuitive.luciuscore.model.v4_1.treatments

import com.dataintuitive.luciuscore.model.v4_1.{TRT, TRT_GENERIC}

/**
  * Treatment case class for perturbagen type trt_lig: 'Peptides and other biological agents (e.g. cytokine)'
  */
case class TRT_LIG(
                    id: String,
                    name: String,
                    dose: String,
                    doseUnit: String,
                    time: String,
                    timeUnit: String
                  ) extends TRT(trtType = "trt_lig") with Serializable {

  type T = TRT_LIG

  def toGeneric:TRT_GENERIC =
    TRT_GENERIC(
      trtType = "trt_lig",
      id = id,
      name = name,
      inchikey = None,
      smiles = None,
      pubchemId = None,
      dose = Some(dose),
      doseUnit = Some(doseUnit),
      time = Some(time),
      timeUnit = Some(timeUnit),
      targets = None
    )

}

/**
  * Treatment companion object for perturbagen type trt_lig: 'Peptides and other biological agents (e.g. cytokine)'
  */
object TRT_LIG {

  def apply(generic: TRT_GENERIC):TRT_LIG =

    TRT_LIG(
      id = generic.id,
      name = generic.name,
      dose = generic.dose.getOrElse("NA"),
      doseUnit = generic.doseUnit.getOrElse("NA"),
      time = generic.time.getOrElse("NA"),
      timeUnit = generic.timeUnit.getOrElse("NA")
    )

}
