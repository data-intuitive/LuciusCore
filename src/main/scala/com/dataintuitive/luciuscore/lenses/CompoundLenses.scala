package com.dataintuitive.luciuscore.lenses

import scalaz.Lens
import com.dataintuitive.luciuscore.Model._

object CompoundLenses extends Serializable {

    val jnjsLens = Lens.lensu[Compound, Option[String]](
        (a, value) => a.copy(jnjs = value),
        _.jnjs
    )
    val jnjbLens = Lens.lensu[Compound, Option[String]](
        (a, value) => a.copy(jnjb = value),
        _.jnjb
    )
    val smilesLens = Lens.lensu[Compound, Option[String]](
        (a, value) => a.copy(smiles = value),
        _.smiles
    )
    val inchikeyLens = Lens.lensu[Compound, Option[String]](
        (a, value) => a.copy(inchikey = value),
        _.inchikey
    )
    val nameLens = Lens.lensu[Compound, Option[String]](
        (a, value) => a.copy(name = value),
        _.name
    )
    val ctypeLens = Lens.lensu[Compound, Option[String]](
        (a, value) => a.copy(ctype = value),
        _.ctype
    )

}
