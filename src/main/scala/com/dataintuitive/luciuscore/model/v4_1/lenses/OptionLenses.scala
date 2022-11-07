package com.dataintuitive.luciuscore
package model.v4_1
package lenses

import genes._

import scalaz.Lens

object OptionLenses extends Serializable {

  val stringLens = Lens.lensu[Option[String], String](
    (a, value) => Some(value),
    _.getOrElse("N/A")
  )

  val listLens = Lens.lensu[Option[List[String]], List[String]](
    (a, value) => Some(value),
    _.getOrElse(Nil)
  )

  def safeStringLens(fallback: String = "OOPS") = Lens.lensu[Option[String], String](
    (a, value) => Some(value),
    _.getOrElse(fallback)
  )

  def safeIntLens(fallback: Int = -1) = Lens.lensu[Option[Int], Int](
    (a, value) => Some(value),
    _.getOrElse(fallback)
  )

  def serializeStringSeqLens = Lens.lensu[Seq[Option[String]], Option[String]](
    (a, value) => value.map(
      _.split("\\|", -1)
        .map(Some(_).filter(_.trim.nonEmpty)) // Map String to Option[String] unless it's empty, then None
        .toSeq
    ).getOrElse(Seq.empty),
    a => 
      if (a.count(_.isDefined) > 0)
        Some(a.map(_.getOrElse("")).mkString("|"))
      else
        Some("")
  )

  def safeSeqStringLens = Lens.lensu[Seq[Option[String]], Seq[String]](
    (a, value) => value.map(Some(_)),
    a => a.map(_.getOrElse("N/A"))
  )

}
