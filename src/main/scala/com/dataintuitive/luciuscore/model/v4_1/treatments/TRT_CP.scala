package com.dataintuitive.luciuscore.model.v4_1.treatments

import com.dataintuitive.luciuscore.model.v4_1.{TRT, TRT_GENERIC}

/**
  * Treatment case class for perturbagen type trt_cp: 'Compound'
  */
case class TRT_CP(
                   id: String,
                   name: String,
                   dose: String,
                   doseUnit: String,
                   time: String,
                   timeUnit: String,
                   inchikey: Option[String],
                   smiles: Option[String],
                   pubchemId: Option[String],
                   targets: List[String]
                 ) extends TRT(trtType = "trt_cp") with Serializable {

  type T = TRT_CP

  def toGeneric:TRT_GENERIC =
    TRT_GENERIC(
      trtType = "trt_cp",
      id = id,
      name = name,
      inchikey = inchikey,
      smiles = smiles,
      pubchemId = pubchemId,
      dose = Some(dose),
      doseUnit = Some(doseUnit),
      time = Some(time),
      timeUnit = Some(timeUnit),
      targets = Some(targets)
    )

}

/**
  * Treatment companion object for perturbagen type trt_cp: 'Compound'
  */
object TRT_CP {

  def apply(generic:TRT_GENERIC):TRT_CP =
    TRT_CP(
      id = generic.id,
      name = generic.name,
      dose = generic.dose.getOrElse("NA"),
      doseUnit = generic.doseUnit.getOrElse("NA"),
      time = generic.time.getOrElse("NA"),
      timeUnit = generic.timeUnit.getOrElse("NA"),
      inchikey = generic.inchikey,
      smiles = generic.smiles,
      pubchemId = generic.pubchemId,
      targets = generic.targets.getOrElse(Nil)
    )

}
