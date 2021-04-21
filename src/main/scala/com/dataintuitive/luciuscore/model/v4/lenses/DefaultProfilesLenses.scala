package com.dataintuitive.luciuscore
package model.v4
package lenses

import scalaz.Lens

object DefaultProfilesLenses extends Serializable {

  val profileLens = Lens.lensu[Profiles, Profile](
      (a, value) => Profiles( if (a.profiles.length <= 1) List(value) else a.profiles ),
      _.profiles.lift(0).getOrElse(Profile())
      )
}
