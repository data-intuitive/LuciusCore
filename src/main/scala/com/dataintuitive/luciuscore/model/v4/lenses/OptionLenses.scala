package com.dataintuitive.luciuscore.model.v4.lenses

import scalaz.Lens
import com.dataintuitive.luciuscore.model.v4.Model._
import com.dataintuitive.luciuscore.genes._

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
