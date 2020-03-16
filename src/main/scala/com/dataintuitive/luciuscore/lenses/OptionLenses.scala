package com.dataintuitive.luciuscore.lenses

import scalaz.Lens
import com.dataintuitive.luciuscore.Model._
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
    val targetsLens = Lens.lensu[Option[Seq[GeneType]], Seq[GeneType]](
        (a, value) => Some(value),
        _.getOrElse(Seq())
    )
    def safeTargetsLens(fallback: Seq[String] = Seq()) = Lens.lensu[Option[Seq[GeneType]], Seq[GeneType]](
            (a, value) => Some(value),
            _.getOrElse(fallback)
        )
}
