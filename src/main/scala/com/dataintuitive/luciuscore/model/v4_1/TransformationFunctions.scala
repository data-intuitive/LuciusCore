package com.dataintuitive.luciuscore
package model.v4_1

import correlations.ZhangScoreFunctions._

object DbFunctions {

  /**
    * Calculate the zhang score between a database entry and a rank vector
    * @param x DbRow
    * @param query Rank vector
    * @return Option-wrapped ScoredPerturbation
    */
  def addScore(x: Perturbation,
               query: Array[Double]): Option[ScoredPerturbation] = {
    x.profiles.profile.flatMap(profile => profile.r match {
      case Some(r) => Some(ScoredPerturbation(connectionScore(r, query), x))
      case _       => None
    })
  }

  /**
    * Calculate the zhang scores between a database entry and list of rank vectors
    * @param x DbRow
    * @param query Rank vectors
    * @return Option-wrapped ScoredPerturbation
    */
  def addScores(x: Perturbation,
                queries: List[Array[Double]]): Option[ScoredPerturbation] = {
    x.profiles.profile.flatMap(profile => profile.r match {
      case Some(r) => Some(ScoredPerturbation(queries.map(q => connectionScore(r, q)), x))
      case _       => None
    })
  }
}
