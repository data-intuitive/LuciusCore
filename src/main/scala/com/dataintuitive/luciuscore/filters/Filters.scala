package com.dataintuitive.luciuscore.filters

trait FiltersTrait extends Serializable {

  /**
   * A basic representation of a filter with a key and a value
   */
  case class Filter(key: String, value:String) extends Serializable

  /**
   * A representation for a filter query: one key, multiple (possible) values
   */
  case class QFilter(val key: String, val values: Seq[String]) extends Serializable

}

// object Filters extends FiltersTrait
