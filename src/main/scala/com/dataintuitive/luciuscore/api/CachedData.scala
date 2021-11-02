package com.dataintuitive.luciuscore
package api

import com.dataintuitive.luciuscore.filters.Filters
import model.v4._
import genes._
import org.apache.spark.sql.Dataset

case class CachedData(db: Dataset[Perturbation], flatDb: Dataset[FlatDbRow], genes: GenesDB, filters:  Filters) extends Serializable
