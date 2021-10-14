package com.dataintuitive.luciuscore.model.v4

import Treatment._
import treatments._

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should._


class TreatmentTest extends AnyFlatSpec with Matchers {

  val genericTrtCp =
    TRT_GENERIC(
      "trt_cp",
      "pertId",
      "pertName",
      Some("inchikey"),
      Some("smiles"),
      Some("pubchem"),
      Some("1"),
      Some("ml"),
      Some("4"),
      Some("m"),
      Some(List("T1", "T2"))
    )

  val genericTrtLig =
    TRT_GENERIC(
      "trt_lig",
      "pertId",
      "pertName",
      None,
      None,
      None,
      Some("2"),
      Some("ml"),
      Some("1"),
      Some("m"),
      None
    )

  val genericTrtSh =
    TRT_GENERIC(
      "trt_sh",
      "pertId",
      "pertName",
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      None
    )

  val genericTrtShCgs =
    TRT_GENERIC(
      "trt_sh.cgs",
      "pertId",
      "pertName",
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      None
    )

  val genericTrtOe =
    TRT_GENERIC(
      "trt_oe",
      "pertId",
      "pertName",
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      None
    )

  val genericTrtOeMut =
    TRT_GENERIC(
      "trt_oe.mut",
      "pertId",
      "pertName",
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      None
    )

  val genericTrtXpr =
    TRT_GENERIC(
      "trt_xpr",
      "pertId",
      "pertName",
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      None
    )

  val genericCtlVehicle =
    TRT_GENERIC(
      "ctl_vehicle",
      "pertId",
      "pertName",
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      None
    )

  val genericCtlVector =
    TRT_GENERIC(
      "ctl_vector",
      "pertId",
      "pertName",
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      None
    )

  val genericTrtShCss =
    TRT_GENERIC(
      "trt_sh.css",
      "pertId",
      "pertName",
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      None
    )

  val genericCtlVehicleCns =
    TRT_GENERIC(
      "ctl_vehicle.cns",
      "pertId",
      "pertName",
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      None
    )

  val genericCtlVectorCns =
    TRT_GENERIC(
      "ctl_vector.cns",
      "pertId",
      "pertName",
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      None
    )

  val genericCtlUntrtCns =
    TRT_GENERIC(
      "ctl_untrt.cns",
      "pertId",
      "pertName",
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      None
    )

  val genericCtlUntrt =
    TRT_GENERIC(
      "ctl_untrt",
      "pertId",
      "pertName",
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      None
    )

  val trtCp = TRT_CP(
    "pertId",
    "pertName",
    "1",
    "ml",
    "4",
    "m",
    Some("inchikey"),
    Some("smiles"),
    Some("pubchem"),
    List("T1", "T2")
  )

  val trtLig = TRT_LIG(
    "pertId",
    "pertName",
    "2",
    "ml",
    "1",
    "m"
  )

  val trtSh = TRT_SH(
    "pertId",
    "pertName"
  )

  val trtShCgs = TRT_SH_CGS(
    "pertId",
    "pertName"
  )

  val trtOe = TRT_OE(
    "pertId",
    "pertName"
  )

  val trtOeMut = TRT_OE_MUT(
    "pertId",
    "pertName"
  )

  val trtXpr = TRT_XPR(
    "pertId",
    "pertName"
  )

  val ctlVehicle = CTL_VEHICLE(
    "pertId",
    "pertName"
  )

  val ctlVector = CTL_VECTOR(
    "pertId",
    "pertName"
  )

  val trtShCss = TRT_SH_CSS(
    "pertId",
    "pertName"
  )

  val ctlVehicleCns = CTL_VEHICLE_CNS(
    "pertId",
    "pertName"
  )

  val ctlVectorCns = CTL_VECTOR_CNS(
    "pertId",
    "pertName"
  )

  val ctlUntrtCns = CTL_UNTRT_CNS(
    "pertId",
    "pertName"
  )

  val ctlUntrt = CTL_UNTRT(
    "pertId",
    "pertName"
  )

  val testTreatmentData = List(
    ("trt_cp",          genericTrtCp,         trtCp,         Treatment(None, trt_cp = Some(trtCp))),
    ("trt_lig",         genericTrtLig,        trtLig,        Treatment(None, trt_lig = Some(trtLig))),
    ("trt_sh",          genericTrtSh,         trtSh,         Treatment(None, trt_sh = Some(trtSh))),
    ("trt_sh.cgs",      genericTrtShCgs,      trtShCgs,      Treatment(None, trt_sh_cgs = Some(trtShCgs))),
    ("trt_oe",          genericTrtOe,         trtOe,         Treatment(None, trt_oe = Some(trtOe))),
    ("trt_oe.mut",      genericTrtOeMut,      trtOeMut,      Treatment(None, trt_oe_mut = Some(trtOeMut))),
    ("trt_xpr",         genericTrtXpr,        trtXpr,        Treatment(None, trt_xpr = Some(trtXpr))),
    ("ctl_vehicle",     genericCtlVehicle,    ctlVehicle,    Treatment(None, ctl_vehicle = Some(ctlVehicle))),
    ("ctl_vector",      genericCtlVector,     ctlVector,     Treatment(None, ctl_vector = Some(ctlVector))),
    ("trt_sh.css",      genericTrtShCss,      trtShCss,      Treatment(None, trt_sh_css = Some(trtShCss))),
    ("ctl_vehicle.cns", genericCtlVehicleCns, ctlVehicleCns, Treatment(None, ctl_vehicle_cns = Some(ctlVehicleCns))),
    ("ctl_vector.cns",  genericCtlVectorCns,  ctlVectorCns,  Treatment(None, ctl_vector_cns = Some(ctlVectorCns))),
    ("ctl_untrt.cns",   genericCtlUntrtCns,   ctlUntrtCns,   Treatment(None, ctl_untrt_cns = Some(ctlUntrtCns))),
    ("ctl_untrt",       genericCtlUntrt,      ctlUntrt,      Treatment(None, ctl_untrt = Some(ctlUntrt)))
  )

  val t = Treatment(Some(TRT_EMPTY))

  "Minimal instantiation" should "just work" in {
    t.trt            shouldBe TRT_EMPTY
    t.toGeneric.trt  shouldBe TRT_EMPTY
    t.toSpecific.trt shouldBe TRT_EMPTY
    t.trt.name       shouldBe "NA"
  }

  "Full instantiation" should "just work" in {

    Treatment(None, Some(trtCp), None).trt_cp    shouldBe Some(trtCp)
    Treatment(None, Some(trtCp), None).trt       shouldBe a [TRT_CP]

  }

  "Treatment constructor" should "create the correct representation for trt_cp" in {

    val trtCp_ = TRT_CP(
        id = "pertId",
        name = "pertName",
        dose = "1",
        doseUnit = "ml",
        time = "4",
        timeUnit = "m",
        inchikey = Some("inchikey"),
        smiles = Some("smiles"),
        pubchemId = Some("pubchem"),
        targets = List("T1", "T2")
      )

    val testTreatment = Treatment(Some(genericTrtCp))

    testTreatment.trt             shouldBe a [TRT_GENERIC]
    testTreatment.toSpecific.trt  shouldBe a [TRT_CP]
    testTreatment.toSpecific.trt  shouldBe trtCp_
    testTreatment.inchikey        shouldBe Some("inchikey")
    testTreatment.smiles          shouldBe Some("smiles")
    testTreatment.pubchemId       shouldBe Some("pubchem")
    testTreatment.dose            shouldBe Some("1")
    testTreatment.doseUnit        shouldBe Some("ml")
    testTreatment.time            shouldBe Some("4")
    testTreatment.timeUnit        shouldBe Some("m")
    testTreatment.targets         shouldBe Some(List("T1", "T2"))

  }

  it should "create the correct representation for trt_lig" in {

    val trtLig_ = TRT_LIG(
        id = "pertId",
        name = "pertName",
        dose = "2",
        doseUnit = "ml",
        time = "1",
        timeUnit = "m"
      )

    val testTreatment = Treatment(Some(genericTrtLig))

    testTreatment.trt             shouldBe a [TRT_GENERIC]
    testTreatment.toSpecific.trt  shouldBe a [TRT_LIG]
    testTreatment.toSpecific.trt  shouldBe trtLig_
    testTreatment.inchikey        shouldBe None
    testTreatment.smiles          shouldBe None
    testTreatment.pubchemId       shouldBe None
    testTreatment.dose            shouldBe Some("2")
    testTreatment.doseUnit        shouldBe Some("ml")
    testTreatment.time            shouldBe Some("1")
    testTreatment.timeUnit        shouldBe Some("m")
    testTreatment.targets         shouldBe None

  }

  it should "create the correct representation for trt_sh" in {

    val trtSh_ = TRT_SH(
      id = "pertId",
      name = "pertName"
    )

    val testTreatment = Treatment(Some(genericTrtSh))

    testTreatment.trt             shouldBe a [TRT_GENERIC]
    testTreatment.toSpecific.trt  shouldBe a [TRT_SH]
    testTreatment.toSpecific.trt  shouldBe trtSh_
    testTreatment.inchikey        shouldBe None
    testTreatment.smiles          shouldBe None
    testTreatment.pubchemId       shouldBe None
    testTreatment.dose            shouldBe None
    testTreatment.doseUnit        shouldBe None
    testTreatment.time            shouldBe None
    testTreatment.timeUnit        shouldBe None
    testTreatment.targets         shouldBe None

  }

  it should "create the correct representation for trt_sh.cgs" in {

    val trtShCgs_ = TRT_SH_CGS(
      id = "pertId",
      name = "pertName"
    )

    val testTreatment = Treatment(Some(genericTrtShCgs))

    testTreatment.trt             shouldBe a [TRT_GENERIC]
    testTreatment.toSpecific.trt  shouldBe a [TRT_SH_CGS]
    testTreatment.toSpecific.trt  shouldBe trtShCgs_
    testTreatment.inchikey        shouldBe None
    testTreatment.smiles          shouldBe None
    testTreatment.pubchemId       shouldBe None
    testTreatment.dose            shouldBe None
    testTreatment.doseUnit        shouldBe None
    testTreatment.time            shouldBe None
    testTreatment.timeUnit        shouldBe None
    testTreatment.targets         shouldBe None

  }

  it should "create the correct representation for trt_oe" in {

    val trtOe_ = TRT_OE(
      id = "pertId",
      name = "pertName"
    )

    val testTreatment = Treatment(Some(genericTrtOe))

    testTreatment.trt             shouldBe a [TRT_GENERIC]
    testTreatment.toSpecific.trt  shouldBe a [TRT_OE]
    testTreatment.toSpecific.trt  shouldBe trtOe_
    testTreatment.inchikey        shouldBe None
    testTreatment.smiles          shouldBe None
    testTreatment.pubchemId       shouldBe None
    testTreatment.dose            shouldBe None
    testTreatment.doseUnit        shouldBe None
    testTreatment.time            shouldBe None
    testTreatment.timeUnit        shouldBe None
    testTreatment.targets         shouldBe None

  }

  it should "create the correct representation for trt_oe.mut" in {

    val trtOeMut_ = TRT_OE_MUT(
      id = "pertId",
      name = "pertName"
    )

    val testTreatment = Treatment(Some(genericTrtOeMut))

    testTreatment.trt             shouldBe a [TRT_GENERIC]
    testTreatment.toSpecific.trt  shouldBe a [TRT_OE_MUT]
    testTreatment.toSpecific.trt  shouldBe trtOeMut_
    testTreatment.inchikey        shouldBe None
    testTreatment.smiles          shouldBe None
    testTreatment.pubchemId       shouldBe None
    testTreatment.dose            shouldBe None
    testTreatment.doseUnit        shouldBe None
    testTreatment.time            shouldBe None
    testTreatment.timeUnit        shouldBe None
    testTreatment.targets         shouldBe None

  }

  it should "create the correct representation for trt_xpr" in {

    val trtXpr_ = TRT_XPR(
      id = "pertId",
      name = "pertName"
    )

    val testTreatment = Treatment(Some(genericTrtXpr))

    testTreatment.trt             shouldBe a [TRT_GENERIC]
    testTreatment.toSpecific.trt  shouldBe a [TRT_XPR]
    testTreatment.toSpecific.trt  shouldBe trtXpr_
    testTreatment.inchikey        shouldBe None
    testTreatment.smiles          shouldBe None
    testTreatment.pubchemId       shouldBe None
    testTreatment.dose            shouldBe None
    testTreatment.doseUnit        shouldBe None
    testTreatment.time            shouldBe None
    testTreatment.timeUnit        shouldBe None
    testTreatment.targets         shouldBe None
  }

  it should "create the correct representation for ctl_vehicle" in {

    val ctlVehicle_ = CTL_VEHICLE(
      id = "pertId",
      name = "pertName"
    )

    val testTreatment = Treatment(Some(genericCtlVehicle))

    testTreatment.trt             shouldBe a [TRT_GENERIC]
    testTreatment.toSpecific.trt  shouldBe a [CTL_VEHICLE]
    testTreatment.toSpecific.trt  shouldBe ctlVehicle_
    testTreatment.inchikey        shouldBe None
    testTreatment.smiles          shouldBe None
    testTreatment.pubchemId       shouldBe None
    testTreatment.dose            shouldBe None
    testTreatment.doseUnit        shouldBe None
    testTreatment.time            shouldBe None
    testTreatment.timeUnit        shouldBe None
    testTreatment.targets         shouldBe None
  }

  it should "create the correct representation for ctl_vector" in {

    val ctlVector_ = CTL_VECTOR(
      id = "pertId",
      name = "pertName"
    )

    val testTreatment = Treatment(Some(genericCtlVector))

    testTreatment.trt             shouldBe a [TRT_GENERIC]
    testTreatment.toSpecific.trt  shouldBe a [CTL_VECTOR]
    testTreatment.toSpecific.trt  shouldBe ctlVector_
    testTreatment.inchikey        shouldBe None
    testTreatment.smiles          shouldBe None
    testTreatment.pubchemId       shouldBe None
    testTreatment.dose            shouldBe None
    testTreatment.doseUnit        shouldBe None
    testTreatment.time            shouldBe None
    testTreatment.timeUnit        shouldBe None
    testTreatment.targets         shouldBe None
  }

  it should "create the correct representation for trt_sh.css" in {

    val trtShCss_ = TRT_SH_CSS(
      id = "pertId",
      name = "pertName"
    )

    val testTreatment = Treatment(Some(genericTrtShCss))

    testTreatment.trt             shouldBe a [TRT_GENERIC]
    testTreatment.toSpecific.trt  shouldBe a [TRT_SH_CSS]
    testTreatment.toSpecific.trt  shouldBe trtShCss_
    testTreatment.inchikey        shouldBe None
    testTreatment.smiles          shouldBe None
    testTreatment.pubchemId       shouldBe None
    testTreatment.dose            shouldBe None
    testTreatment.doseUnit        shouldBe None
    testTreatment.time            shouldBe None
    testTreatment.timeUnit        shouldBe None
    testTreatment.targets         shouldBe None
  }

  it should "create the correct representation for ctl_vehicle.cns" in {

    val ctlVehicleCns_ = CTL_VEHICLE_CNS(
      id = "pertId",
      name = "pertName"
    )

    val testTreatment = Treatment(Some(genericCtlVehicleCns))

    testTreatment.trt             shouldBe a [TRT_GENERIC]
    testTreatment.toSpecific.trt  shouldBe a [CTL_VEHICLE_CNS]
    testTreatment.toSpecific.trt  shouldBe ctlVehicleCns_
    testTreatment.inchikey        shouldBe None
    testTreatment.smiles          shouldBe None
    testTreatment.pubchemId       shouldBe None
    testTreatment.dose            shouldBe None
    testTreatment.doseUnit        shouldBe None
    testTreatment.time            shouldBe None
    testTreatment.timeUnit        shouldBe None
    testTreatment.targets         shouldBe None
  }

  it should "create the correct representation for ctl_vector.cns" in {

    val ctlVectorCns_ = CTL_VECTOR_CNS(
      id = "pertId",
      name = "pertName"
    )

    val testTreatment = Treatment(Some(genericCtlVectorCns))

    testTreatment.trt             shouldBe a [TRT_GENERIC]
    testTreatment.toSpecific.trt  shouldBe a [CTL_VECTOR_CNS]
    testTreatment.toSpecific.trt  shouldBe ctlVectorCns_
    testTreatment.inchikey        shouldBe None
    testTreatment.smiles          shouldBe None
    testTreatment.pubchemId       shouldBe None
    testTreatment.dose            shouldBe None
    testTreatment.doseUnit        shouldBe None
    testTreatment.time            shouldBe None
    testTreatment.timeUnit        shouldBe None
    testTreatment.targets         shouldBe None
  }

  it should "create the correct representation for ctl_untrt.cns" in {

    val ctlUntrtCns_ = CTL_UNTRT_CNS(
      id = "pertId",
      name = "pertName"
    )

    val testTreatment = Treatment(Some(genericCtlUntrtCns))

    testTreatment.trt             shouldBe a [TRT_GENERIC]
    testTreatment.toSpecific.trt  shouldBe a [CTL_UNTRT_CNS]
    testTreatment.toSpecific.trt  shouldBe ctlUntrtCns_
    testTreatment.inchikey        shouldBe None
    testTreatment.smiles          shouldBe None
    testTreatment.pubchemId       shouldBe None
    testTreatment.dose            shouldBe None
    testTreatment.doseUnit        shouldBe None
    testTreatment.time            shouldBe None
    testTreatment.timeUnit        shouldBe None
    testTreatment.targets         shouldBe None
  }

  it should "create the correct representation for ctl_untrt" in {

    val ctlUntrt_ = CTL_UNTRT(
      id = "pertId",
      name = "pertName"
    )

    val testTreatment = Treatment(Some(genericCtlUntrt))

    testTreatment.trt             shouldBe a [TRT_GENERIC]
    testTreatment.toSpecific.trt  shouldBe a [CTL_UNTRT]
    testTreatment.toSpecific.trt  shouldBe ctlUntrt_
    testTreatment.inchikey        shouldBe None
    testTreatment.smiles          shouldBe None
    testTreatment.pubchemId       shouldBe None
    testTreatment.dose            shouldBe None
    testTreatment.doseUnit        shouldBe None
    testTreatment.time            shouldBe None
    testTreatment.timeUnit        shouldBe None
    testTreatment.targets         shouldBe None

  }

  it should "allow transformation between specific and generic representations" in {

    val trt = Treatment(Some(genericTrtCp))

    trt.toSpecific.toGeneric shouldBe trt

  }

  "TRT_EMPTY" should "represent an NA value" in {

    t.trt      shouldBe TRT_EMPTY
    t.trt.id   shouldBe "NA"
    t.trt.name shouldBe "NA"
    t.isEmpty  shouldBe true
    t.trt.id   shouldBe "NA"
    t.trt.name shouldBe "NA"

  }

  "All generic treatments" should "have the correct type name in the base class" in {

    for ((name, generic, _, _) <- testTreatmentData) {
      generic.trtType shouldBe name
    }

  }

  it should "have trt_generic as the type name in the super class " in {

    for ((_, generic, _, _) <- testTreatmentData) {
      Treatment(Some(generic)).trtType shouldBe "trt_generic"
    }

  }

  it should "have specific name as the type name in the super class trt" in {

    for ((name, generic, _, _) <- testTreatmentData) {
      Treatment(Some(generic)).trt.trtType shouldBe name
    }

  }

  it should "match already present treatments base class" in {

    for ((_, generic, _, _) <- testTreatmentData) {
      Treatment(Some(generic)).trt shouldBe generic
    }

  }

  it should "transform to generic and specific treatments" in {

    for ((_, generic, specific, _) <- testTreatmentData) {
      Treatment(Some(generic)).toGeneric.trt  shouldBe generic
      Treatment(Some(generic)).toSpecific.trt shouldBe specific
    }

  }

  "All specific treatments" should "have the correct type name once transformed to generic treatments" in {

    for ((name, generic, _, specificTrt) <- testTreatmentData) {
      specificTrt.toGeneric.trt.trtType                         shouldBe name
      Treatment(Some(generic)).toSpecific.toGeneric.trt.trtType shouldBe name
    }

  }

  it should "match already present treatments base class" in {

    for ((_, _, specific, specificTrt) <- testTreatmentData) {
      specificTrt.trt shouldBe specific
    }

  }

  it should "transform to generic and generic treatments" in {

    for ((_, generic, specific, specificTrt) <- testTreatmentData) {
      specificTrt.toGeneric.trt                         shouldBe generic
      specificTrt.toSpecific.trt                        shouldBe specific
      Treatment(Some(generic)).toSpecific.toGeneric.trt shouldBe generic
    }

  }

}
