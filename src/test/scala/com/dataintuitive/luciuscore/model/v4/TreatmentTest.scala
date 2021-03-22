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

  val trtLig = TRT_LIG(
      "pertId",
      "pertName",
      "2",
      "ml",
      "1",
      "m"
    )

  "Full instances" should "just work" in {
    Treatment(None, Some(trtCp), None).trt_cp    shouldBe Some(trtCp)
    Treatment(None, Some(trtCp), None).trt       shouldBe a [TRT_CP]
    Treatment(None, Some(trtCp), None).trt       shouldBe a [TRT_CP]
    Treatment(Some(trtGenericCp)).trt            shouldBe a [TRT_GENERIC]
    Treatment(Some(trtGenericCp)).toSpecific.trt shouldBe a [TRT_CP]
    Treatment(Some(trtGenericCp)).toSpecific.trt shouldBe trtCp
  }

}
