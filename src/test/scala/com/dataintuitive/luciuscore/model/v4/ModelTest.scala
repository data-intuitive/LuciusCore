package com.dataintuitive.luciuscore.model.v4

import Treatment._

import org.scalatest.flatspec.AnyFlatSpec


class ModelTestv4 extends AnyFlatSpec {

  info("Test v4 Model")

  val p = Perturbation("123", Information(), Profiles(), TRT_EMPTY, Nil)

  // "Minimal Instantiation" should "simply work" in {
  //   assert(p.id === "123")
  //   assert(p.trtType === "empty")
  //   assert(p.trt === TRT_EMPTY)
  //   assert(p.trt_cp === None)
  // }

  // "Full instantiation" should "automatically create the correct entries" in {
  //   assert(cp.trt !== None)
  //   assert(cp.trt_lig === None)
  // }

  // "After init, trt_generic" should "be converted and turned to None" in {
  //   assert(cp.trt_cp !== None)
  //   assert(cp.trt_generic === None)
  //   assert(lig.trt_lig !== None)
  //   assert(lig.trt_generic === None)
  // }

  // it should "allow for using accessor methods trt and trtSafe" in {
  //   assert(cp.trt === cp.trt_cp.get)
  //   assert(cp.trtSafe === cp.trt_cp.get)
  //   assert(lig.trt === lig.trt_lig.get)
  //   assert(lig.trtSafe === lig.trt_lig.get)
  // }

}
