package com.dataintuitive.luciuscore
package api.v4_1

import com.dataintuitive.luciuscore.api.v4_1.Filters.FiltersDB
import model.v4_1._
import genes._
import org.apache.spark.sql.Dataset

case class CachedData(db: Dataset[Perturbation], flatDb: Dataset[FlatDbRow], genes: GenesDB, filters:  FiltersDB) extends Serializable
