package com.dataintuitive.luciuscore
package model.v4_1
package lenses

import scalaz.Lens

object ProfileLenses extends Serializable {

  val pTypeLens = Lens.lensu[Profile, String](
      (a, value) => a.copy(pType = value),
      _.pType
    )

  val lengthLens = Lens.lensu[Profile, Int](
      (a, value) => a.copy(length = value),
      _.length
    )

  val tLens = Lens.lensu[Profile, Option[Array[Double]]](
      (a, value) => a.copy(t = value),
      _.t
    )

  val pLens = Lens.lensu[Profile, Option[Array[Double]]](
      (a, value) => a.copy(p = value),
      _.p
    )

  val logFcLens = Lens.lensu[Profile, Option[Array[Double]]](
      (a, value) => a.copy(logFc = value),
      _.logFc
    )

  val rLens = Lens.lensu[Profile, Option[Array[Double]]](
      (a, value) => a.copy(r = value),
      _.r
    )

}
