package com.dataintuitive.luciuscore
package model.v3
package lenses

import scalaz.Lens

object CompoundLenses extends Serializable {

    val idLens = Lens.lensu[Compound, Option[String]](
        (a, value) => a.copy(id = value),
        _.id
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

    // Pending deprecation
    val jnjsLens = Lens.lensu[Compound, Option[String]](
        (a, value) => a.copy(id = value),
        _.id
    )
    val jnjbLens = Lens.lensu[Compound, Option[String]](
        (a, value) => a.copy(id = value),
        _.id
    )

}
