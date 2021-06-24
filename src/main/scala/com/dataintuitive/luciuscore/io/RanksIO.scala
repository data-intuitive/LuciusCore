package com.dataintuitive.luciuscore
package io

import model.v3._
import TransformationFunctions._

import org.apache.spark.rdd.RDD


/**
  * Convenience object that handles the update of the db with ranks.
  */
object RanksIO extends Serializable {

  /**
    * Update the ranks in an immutable and safe way for one sample.
    * Only when both p and t vectors are well-defined is a rank vector created.
    */
  def updateRanks(x:DbRow):DbRow =   { (x.sampleAnnotations.t, x.sampleAnnotations.p) match {

    case (Some(t), Some(p)) =>  x.copy(
      sampleAnnotations=x.sampleAnnotations.copy(
        r=Some(stats2RankVector((t, p)))
      )
    )
    case _ => x
  }
  }

  /**
    * Update the ranks in an immutable and safe way for an RDD of samples.
    * Only when both p and t vectors are well-defined is a rank vector created.
    */
  def updateRanks(x:RDD[DbRow]):RDD[DbRow] =  x.map((x:DbRow) => updateRanks(x))

}
