package com.dataintuitive.luciuscore

/**
 * A basic representation of a filter with a key and a value
 */
case class Filter(key: String, value:String) extends Serializable

/**
 * A class to contain multiple filters to match on and two helper functions.
 */
case class Filters(filters:Seq[Filter] = Nil) extends Serializable {

  // Check if there is a match, return true if no filters are available
  def isMatch(query:Filter):Boolean =
    if (!filters.isEmpty)
      filters.map(_ equals query).reduce(_ || _)
    else
      true

  // Check if there is a match, return true if no filters are available
  def isMatch(queries:Seq[Filter]):Boolean =
    if (!filters.isEmpty)
      queries.flatMap(query => filters.map(_ equals query)).reduce(_||_)
    else
      true

  def isEmpty = filters.isEmpty

  def add(newFilter:Filter) =
    if (isMatch(newFilter) && !isEmpty)
      this
    else
      Filters(filters ++ Seq(newFilter))

  def remove(toRemove:Filter) = Filters(filters.filter(f => !(f equals toRemove)))

  def +(newFilter:Filter) = add(newFilter)
  def -(toRemove:Filter) = remove(toRemove)

}
