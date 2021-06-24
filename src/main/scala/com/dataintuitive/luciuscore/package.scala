package com.dataintuitive

package object luciuscore {

  type Value = Double
  type ValueVector = Array[Value]

  /**
   * A RankVector is just an array of Ranks, being Double values.
   *
   * Please note that we wrap signatures but not RankVectors because we can't afford
   * the overhead when running in a distributed way.
   */
  type Rank = Double
  type RankVector = Array[Rank]

  type Index = Int

  /**
   * A generic representation of a row in a datafile
   */
  type Row = Array[Option[String]]

}
