package com.dataintuitive.luciuscore

/**
 * A class to contain multiple filters to match on and two helper functions.
 */
case class Filters(filters:Seq[Filter] = Nil) extends Serializable
