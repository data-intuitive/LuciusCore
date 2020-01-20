package com.dataintuitive.luciuscore.signatures

import Sign._

/**
 * Base trait for a signed entry in a signature. 
 * Can either be symbol, ... and thus String type or
 * an index and thus Int/Index type.
 */
sealed trait SignedType[A<:Any] extends Serializable {

    val sign: Sign
    val abs: A

    override def toString = signString + abs.toString

    def signString:String = sign match {
      case PLUS => ""
      case MINUS => "-"
    }

    def signInt:Int = sign match {
      case PLUS => 1
      case MINUS => -1
    }

}

/**
 * Implementation for Strings
 */
case class SignedString(val sign: Sign, val abs: String) extends SignedType[String]
object SignedString {
  def apply(s:String) = if (s.head == '-') new SignedString(MINUS, s.drop(1)) else new SignedString(PLUS, s)
}

/**
 * Implementation for Int
 */
case class SignedInt(val sign: Sign, val abs: Int) extends SignedType[Int] {
  def toInt = signInt * abs
}
object SignedInt {
  def apply(i:Int) = if (i < 0) new SignedInt(MINUS, -i) else new SignedInt(PLUS, i)
}

