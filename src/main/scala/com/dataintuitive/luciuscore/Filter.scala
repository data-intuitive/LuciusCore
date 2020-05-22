package com.dataintuitive.luciuscore

/**
 * A basic representation of a filter with a key and a value
 */
case class Filter(key: String, value:String) extends Serializable

/**
 * A representation for a filter query: one key, multiple (possible) values
 */
case class QFilter(val key: String, val values: Seq[String]) extends Serializable

/**
 * Helper functions for dealing with Seq[Filter] and QFilter
 */
object FilterFunctions extends Serializable {

  // Retrieve a Set of filter values corresponding to a key
  def getFilterValues(filters: Seq[Filter], key:String):Set[String] = filters.filter(_.key == key).map(_.value).toSet

  // Check a match for 1 query filter key (QFilter)
  def isMatch(qfilter: QFilter, dfilters: Seq[Filter]):Boolean =
      qfilter.values.toSet.intersect(getFilterValues(dfilters, qfilter.key)).size > 0

  // Check a match for Seq[QFilter]
  def isMatch(qfilters: Seq[QFilter], dfilters: Seq[Filter]):Boolean =
      if (qfilters.size > 0)
        qfilters.map(isMatch(_, dfilters)).reduce(_&&_)
      else
        true
}
