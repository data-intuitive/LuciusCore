package com.dataintuitive.luciuscore
package model.v4_1
package lenses

import filters._

import scalaz.Lens

object FilterLenses extends Serializable {

  val filterMapLens = Lens.lensu[Filter, Map[String, String]](
    (a, value) => Filter(a.key, a.value),
    f => Seq(f).map(x => (x.key, x.value)).toMap
  )

  val filterTupleLens = Lens.lensu[Filter, (String, String)](
    (a, value) => Filter(value._1, value._2),
    x => (x.key, x.value)
  )

  val seqFilterMapLens = Lens.lensu[Seq[Filter], Map[String, String]](
    (a, value) => a.map(x => Filter(x.key, x.value)),
    f => f.map(x => (x.key, x.value)).toMap
  )

  val seqFilterTupleLens = Lens.lensu[Seq[Filter], Seq[(String, String)]](
    (a, value) => a.map(x => Filter(x.key, x.value)),
    f => f.map(x => (x.key, x.value))
  )

}
