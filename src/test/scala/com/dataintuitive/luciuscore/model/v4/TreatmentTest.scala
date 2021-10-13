package com.dataintuitive.luciuscore.model.v4

import Treatment._
import treatments._

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should._


class TreatmentTest extends AnyFlatSpec with Matchers {

  val t = Treatment(Some(TRT_EMPTY))

  "Minimal instantiation" should "just work" in {
    assert(t.trt === TRT_EMPTY)
    assert(t.toSpecific.trt === TRT_EMPTY)

  }

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


  "Full instantiation" should "just work" in {

    Treatment(None, Some(trtCp), None).trt_cp    shouldBe Some(trtCp)
    Treatment(None, Some(trtCp), None).trt       shouldBe a [TRT_CP]

  }

  "Treatment constructor" should "create the correct representation for trt_cp" in {

    val trtCp = TRT_CP(
        name = "pertName",
        id = "pertId",
        dose = "1",
        doseUnit = "ml",
        time = "4",
        timeUnit = "m",
        inchikey = Some("inchikey"),
        smiles = Some("smiles"),
        pubchemId = Some("pubchem"),
        targets = List("T1", "T2")
      )

    Treatment(Some(genericTrtCp)).trt             shouldBe a [TRT_GENERIC]
    Treatment(Some(genericTrtCp)).toSpecific.trt  shouldBe a [TRT_CP]
    Treatment(Some(genericTrtCp)).toSpecific.trt  shouldBe trtCp
    Treatment(Some(genericTrtCp)).dose            shouldBe Some("1")
    Treatment(Some(genericTrtCp)).time            shouldBe Some("4")
    Treatment(Some(genericTrtCp)).smiles          shouldBe Some("smiles")

  }

  it should "create the correct representation for trt_lig" in {

    val trtLig = TRT_LIG(
        id = "pertId",
        name = "pertName",
        dose = "2",
        doseUnit = "ml",
        time = "1",
        timeUnit = "m"
      )

    Treatment(Some(genericTrtLig)).trt            shouldBe a [TRT_GENERIC]
    Treatment(Some(genericTrtLig)).toSpecific.trt shouldBe a [TRT_LIG]
    Treatment(Some(genericTrtLig)).toSpecific.trt shouldBe trtLig
    Treatment(Some(genericTrtLig)).dose           shouldBe Some("2")
    Treatment(Some(genericTrtLig)).time           shouldBe Some("1")
    Treatment(Some(genericTrtLig)).smiles         shouldBe None

  }

  it should "allow transformation between specific and generic representations" in {

    val trt = Treatment(Some(genericTrtCp))

    trt.toSpecific.toGeneric shouldBe trt

  }

  "TRT_EMPTY" should "represent an NA value" in {

    t.trt shouldBe TRT_EMPTY
    t.trt.id shouldBe "NA"
    t.trt.name shouldBe "NA"
    t.isEmpty shouldBe true
    t.trt.id shouldBe "NA"
    t.trt.name shouldBe "NA"

  }

  "All generic treatments" should "have the correct name" in {

    for ((name, generic, _, _) <- testTreatmentData) {
      generic.trtType shouldBe name
    }

  }

  it should "transform to specific treatments" in {

    for ((_, generic, specific, _) <- testTreatmentData) {
      Treatment(Some(generic)).toSpecific.trt shouldBe specific
    }

  }

  "All specific treatments" should "transform to generic treatments" in {

    for ((_, generic, _, specific) <- testTreatmentData) {
      specific.toGeneric.trt shouldBe generic
      Treatment(Some(generic)).toSpecific.toGeneric.trt shouldBe generic
    }

  }

}
