package com.dataintuitive.luciuscore

import com.dataintuitive.luciuscore.Model.DbRow
import com.dataintuitive.luciuscore.ZhangScoreFunctions.connectionScore

object DbFunctions {
  /**
    * Score an arbitrary number of rank vectors against a database entry.
    * @param x DbRow
    * @param queries Rank vector(s)
    * @return Map, with DbRows' id as the key an a list of length(queries) as the value
    */
  def queryDbRowPwid(x: DbRow,
                     queries: Array[Double]*): Map[Option[String], Seq[Option[Double]]] = {
    x.sampleAnnotations.r match {
      case Some(r) => Map(x.id ->
        queries.map(query => Option(connectionScore(r, query))))
      case _       => Map(None -> List(None))
    }
  }

  /**
    * Score an arbitrary number of rank vectors against a database entry.
    * @param x DbRow
    * @param queries Rank vector(s)
    * @return List of Option-wrapped tuples (DbRow, Seq(scores))
    */
  def queryDbRow(x: DbRow,
                     queries: Array[Double]*): Option[(DbRow, Seq[Double])] = {
    x.sampleAnnotations.r match {
      case Some(r) => Some(x, queries.map(query => connectionScore(r, query)))
      case _       => None
    }
  }

}
