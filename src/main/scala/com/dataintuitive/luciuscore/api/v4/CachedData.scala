package com.dataintuitive.luciuscore
package api.v4

import com.dataintuitive.luciuscore.api.v4.Filters.FiltersDB
import model.v4._
import genes._
import org.apache.spark.sql.Dataset

case class CachedData(db: Dataset[Perturbation], flatDb: Dataset[FlatDbRow], genes: GenesDB, filters:  FiltersDB) extends Serializable
