package com.dataintuitive.luciuscore.model.v4

import Treatment._

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should._


class TreatmentTest extends AnyFlatSpec with Matchers {

  val t = Treatment(Some(TRT_EMPTY))

  "Minimal instantiation" should "just work" in {
    assert(t.trt === TRT_EMPTY)
    assert(t.toSpecific.trt === TRT_EMPTY)

  }

  val trtGenericCp =
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
      Some("m")
    )

  val trtGenericLig =
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
      Some("m")
    )

  "Full instantiation" should "just work" in {

    val trtCp = TRT_CP(
        "pertId",
        "pertName",
        "1",
        "ml",
        "4",
        "m",
        Some("inchikey"),
        Some("smiles"),
        Some("pubchem")
      )

    Treatment(None, Some(trtCp), None).trt_cp    shouldBe Some(trtCp)
    Treatment(None, Some(trtCp), None).trt       shouldBe a [TRT_CP]
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
        pubchemId = Some("pubchem")
      )

    Treatment(Some(trtGenericCp)).trt             shouldBe a [TRT_GENERIC]
    Treatment(Some(trtGenericCp)).toSpecific.trt  shouldBe a [TRT_CP]
    Treatment(Some(trtGenericCp)).toSpecific.trt  shouldBe trtCp
    Treatment(Some(trtGenericCp)).dose            shouldBe Some("1")
    Treatment(Some(trtGenericCp)).time            shouldBe Some("4")
    Treatment(Some(trtGenericCp)).smiles          shouldBe Some("smiles")

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

    Treatment(Some(trtGenericLig)).trt            shouldBe a [TRT_GENERIC]
    Treatment(Some(trtGenericLig)).toSpecific.trt shouldBe a [TRT_LIG]
    Treatment(Some(trtGenericLig)).toSpecific.trt shouldBe trtLig
    Treatment(Some(trtGenericLig)).dose            shouldBe Some("2")
    Treatment(Some(trtGenericLig)).time            shouldBe Some("1")
    Treatment(Some(trtGenericLig)).smiles          shouldBe None

  }

  "TRT_EMPTY" should "represent an NA value" in {

    t.trt shouldBe TRT_EMPTY
    t.trt.id shouldBe "NA"
    t.trt.name shouldBe "NA"
    t.isEmpty shouldBe true
    t.trt.id shouldBe "NA"
    t.trt.name shouldBe "NA"

  }
}
