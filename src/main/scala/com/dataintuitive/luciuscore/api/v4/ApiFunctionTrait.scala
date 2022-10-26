package com.dataintuitive.luciuscore
package api.v4

import org.apache.spark.sql.SparkSession

trait ApiFunctionTrait extends Serializable {

    type SpecificData
    case class JobData(version: String, cachedData: CachedData, specificData: SpecificData)
    type JobOutput

    val helpMsg:String
    val infoMsg:String
    def help = helpMsg
    def info = infoMsg
    def header: SparkSession =>
      (JobData => JobOutput) = ???
    def result: SparkSession =>
      (JobData => JobOutput) = ???
    def pretty(sparkSession: SparkSession)(data: JobData):Unit = {
        val r = result(sparkSession)(data)
        prettyPrinter(r)
      }

    def prettyPrint(obj: Any, indent: String = ""):String = obj match {
        case obj:List[Any] =>
          obj
            .map(x => prettyPrint(x, indent + "  "))
            .mkString("\n" + indent + "- ", "\n" + indent + "- ", "")
        case obj:Array[Any] =>
          obj
            .map(x => prettyPrint(x, indent + "  "))
            .mkString("\n" + indent + "- ", "\n" + indent + "- ", "")
        case obj:Map[_,_] =>
          obj
            .map(x => prettyPrint(x, indent))
            .mkString("\n" + indent, "\n" + indent, "")
        case obj:(_, _)   => obj._1 + " -> " + prettyPrint(obj._2, indent + "  ")
        case obj:Int             => obj.toString
        case obj:String          => obj
        case obj:Boolean         => obj.toString
        case _ => obj.toString
    }

    def prettyPrinter(obj:Any):Unit = println(prettyPrint(obj, ""))
}
