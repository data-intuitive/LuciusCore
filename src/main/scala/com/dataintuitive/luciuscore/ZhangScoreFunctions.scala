package com.dataintuitive.luciuscore

import scala.math.{abs, max}

/**
  * The functions required for calculating the Zhang score (aka Connection score) between two rank vectors.
  */
object ZhangScoreFunctions {

  /**
    * Calculate the connection score/similarity between two rank vectors.
    *
    * Remark: The order is inmportant: Query vector comes second
    */
  def connectionScore(rv1: RankVector, rv2: RankVector): Double = {
    connectionStrength(rv1, rv2) / maxConnectionStrength(rv1, rv2)
  }

  def connectionStrength(rv1: RankVector, rv2: RankVector): Double =
    rv1
      .zip(rv2)
      .map { case (i, j) => i * j }
      .sum

  def maxConnectionStrength(rv1: RankVector, rv2: RankVector): Double = {
    val maxr = rv1.map(abs(_)).foldLeft(0.0)(max(_, _))
    val maxq = rv2.map(abs(_)).foldLeft(0.0)(max(_, _))
    (maxr to (maxr - maxq) by -1)
      .zip(maxq to 0 by -1)
      .map { case (i, j) => i * j }
      .sum
  }

}
