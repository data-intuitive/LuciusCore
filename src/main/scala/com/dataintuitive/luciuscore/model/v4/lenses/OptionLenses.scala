package com.dataintuitive.luciuscore
package model.v4
package lenses

import genes._

import scalaz.Lens

object OptionLenses extends Serializable {

    val stringLens = Lens.lensu[Option[String], String](
        (a, value) => Some(value),
        _.getOrElse("N/A")
    )
    def safeStringLens(fallback: String = "OOPS") = Lens.lensu[Option[String], String](
            (a, value) => Some(value),
            _.getOrElse(fallback)
        )
}
