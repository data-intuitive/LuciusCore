package com.dataintuitive.luciuscore.io

import scala.util.control.Exception._

case class Version(major: Int, minor: Int) extends Ordered[Version] {
  import scala.math.Ordered.orderingToOrdered

  override def toString = major.toString + "_" + minor.toString

  def compare(that: Version): Int =
    ((this.major compare that.major), (this.minor compare that.minor)) match {
      case (1,_) => 1
      case (0,1) => 1
      case (0,0) => 0
      case _ => -1
    }

  def update(fromScratch: Boolean) =
    if (fromScratch)
      Version(this.major + 1, 0)
    else
      Version(this.major, this.minor + 1)
}

object Version {
  def apply(v:String):Version = {
    val vSplit = v.split("_")
    val major = vSplit.headOption.flatMap(x => allCatch.opt(x.toInt)).getOrElse(0)
    val minor = if (vSplit.size > 1) vSplit.drop(1).headOption.flatMap(x => allCatch.opt(x.toInt)).getOrElse(0) else 0
    Version(major, minor)
  }
}
