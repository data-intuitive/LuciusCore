package com.dataintuitive.luciuscore
package api

trait ApiFunctionTrait {

    type JobData
    type JobOutput

    val helpMsg:String
    val infoMsg:String
    def help = helpMsg
    def info = infoMsg
    def header: org.apache.spark.sql.SparkSession =>
      (JobData => JobOutput) = ???
    def result: org.apache.spark.sql.SparkSession =>
      (JobData => JobOutput) = ???
}
