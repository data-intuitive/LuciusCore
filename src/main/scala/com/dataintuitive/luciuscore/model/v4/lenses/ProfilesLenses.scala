package com.dataintuitive.luciuscore
package model.v4
package lenses

import scalaz.Lens

object ProfilesLenses extends Serializable {

  val defaultProfileLens = Lens.lensu[Profiles, Profile](
    (a, value) => Profiles(List(value)),
    _.profile.get
  )

}
