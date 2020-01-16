package com.dataintuitive.luciuscore.io

import org.apache.spark.rdd.RDD

/**
  * A set of functions that parse an `RDD[String]` by using the column names in the `RDD`.
  *
  * Two variants exist:
  *
  *   1. `retrieveKeyValues` parses the `RDD` and returns key-value pairs
  *
  *   2. `retrieveArray` parses the `RDD` and returns a simple `RDD`
  *
  * The dimension of the returned `RDD` corresponds to the features selected. As a consequence,
  * Providing wrong feature names results in a column of all `None` `Option` values.
  *
  * Please refer to the tests for more information.
  */
object ParseFunctions extends Serializable {

  /**
    * Function to parse an `RDD` and extract columns by name (in header).
    *
    * The result is a key-value pair with Strings wrapped in `Option`.
    *
    * @param rdd The `RDD` to parse
    * @param key The column name in the `RDD` to use as key
    * @param values The value columns (`Seq[String]`) for the values
    * @param includeHeader Add the header row to the resulting `RDD`? Default is `false`.
    * @return `RDD` of key value pairs.
    */
  def extractFeaturesKV(rdd:RDD[Array[String]],
                        key:String,
                        values:Seq[String],
                        includeHeader:Boolean = false
                       ): RDD[(Option[String], Array[Option[String]])] = {
    val header = rdd.first.map(_.trim)
    val keyIndex = header.indexOf(key)
    val valueIndices = values.map(value => header.indexOf(value))
    val selectionRdd = rdd
      .map(row => (row.lift(keyIndex), valueIndices.map(valueIndex => row.lift(valueIndex)).toArray))
    if (!includeHeader)
      selectionRdd.zipWithIndex.filter(_._2 > 0).keys
    else
      selectionRdd
  }

  /**
    * Function to parse an `RDD` and extract columns by name (in header).
    *
    * The result is a square `RDD` with Strings wrapped in `Option`.
    *
    * @param rdd The `RDD` to parse
    * @param features The columns to parse (`Seq[String]`)
    * @param includeHeader Add the header row to the resulting `RDD`? Default is `false`.
    * @return `RDD` of key the selected columns/features.
    */
  def extractFeatures(rdd:RDD[Array[String]],
                      features:Seq[String],
                      includeHeader:Boolean = false
                   ): RDD[Array[Option[String]]] = {
    val header = rdd.first.map(_.trim)          // Be on the safe side !
    val featureIndices = features.map(value => header.indexOf(value))
    val selectionRdd = rdd
      .map(row => featureIndices.map(valueIndex => row.lift(valueIndex)).toArray)
      .map(_.map(_.map(_.replace("\"", ""))))   // Remove quotes
    if (!includeHeader)
      selectionRdd.zipWithIndex.filter(_._2 > 0).keys
    else
      selectionRdd

  }

  /**
   * Load a textfile using some seperator (tab be default)
   */
  def loadTSV(_sc: SparkContext,
              filePath: String,
              delimiter: String = "\t"):RDD[Array[String]] = {

      _sc.textFile(filePath)
        .map(_.split(delimiter))
        .map(_.map(_.trim))

  }
}
