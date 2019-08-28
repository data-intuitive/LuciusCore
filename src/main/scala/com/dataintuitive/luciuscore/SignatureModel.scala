package com.dataintuitive.luciuscore

import com.dataintuitive.luciuscore.genes._
import com.dataintuitive.luciuscore.utilities.SignedString
import com.dataintuitive.luciuscore.Model._

/**
  * A signature is sparse representation of a vector referring to the indices in a dense array.
  *
  * 3 implementations exist, depending on the naming scheme involved:
  *
  *  1. `SymbolSignature` when using gene symbol notation (e.g. MELK, BRCA1, ...).
  *
  *  2. `ProbesetIdSignature` when using the probeset ids used in experiments.
  *
  *  3. `IndexSignature` when using indices referring to the dense vector/array.
  *
  * The natural order of things is: `Symbol -> Probesetid -> Index`
  */
object SignatureModel extends Serializable {

  /**
    * The base trait for a signature
    */
  trait Signature {

    type T
    type U

    val signature: SignatureType

    // Notation can be one of Symbol, Probesetid, Index
    // The flow is Symbol -> Probesetid -> Index and back
    val notation: NotationType

    implicit def stringExtension(string: String) = new SignedString(string)

    override def toString = signature.mkString(s"Signature of type ${notation}: [", ",", "]")

    val length = signature.length

  }

  /**
    * Companion object to the `Signature` trait.
    *
    * This object defines a convencience `apply` method to create a signature of any type:
    *
    * {{{
    * val symbolList = Signature(Array("symbol2", "-symbol1"), "symbol")
    * val probesetidList = Signature(Array("psid2", "-psid1"), "probesetid")
    * }}}
    *
    */
  object Signature {

    /**
      * This will be updated to reflect heuristics that distinguish the different types of
      * signatures. For now, we stick to SymbolSignature as the target type.
      */
    def apply(s: SignatureType): Signature = {
      SymbolSignature(s)
    }

    /**
      * Generate a Signature of provided type/notation.
      */
    def apply(s: SignatureType, notation: String): Signature = {
      notation match {
        case "symbol" => SymbolSignature(s)
        case "probesetid" => ProbesetidSignature(s)
        case "index" => IndexSignature(s)
        case _ => println("Wrong signature type, please try again"); SymbolSignature(s)
      }
    }
  }

  /**
    * Signature with symbol notation.
    */
  case class SymbolSignature(val signature: SignatureType) extends Signature with Serializable {

    val notation: NotationType = SYMBOL

    /**
      * Translate a Symbol signature to a Probesetid signature
      *
      * @param dict     A dictionary (`Map`) `Symbol -> Probesetid`
      * @param failover A placeholder for missing values
      */
    def translate2Probesetid(dict: Map[Symbol, Probesetid], failover: String = "OOPS"): ProbesetidSignature = {
      val safeTranslation = safeTranslate2Probesetid(dict)
      ProbesetidSignature(safeTranslation.map(_.getOrElse(failover)))
    }

    /**
      * Translate a Symbol signature to a Probesetid signature. The result is wrapped in an `Option`.
      *
      * @param dict A dictionary (`Map`) `Symbol -> Probesetid`
      */
    def safeTranslate2Probesetid(dict: Map[Symbol, Probesetid]): Array[Option[Probesetid]] = {
      signature.map { g =>
        val translation = dict.get(g.abs)
        translation.map(go => g.sign + go)
      }
    }

  }

  /**
    * Signature with probesetid notation.
    */
  case class ProbesetidSignature(val signature: SignatureType) extends Signature with Serializable {

    val notation: NotationType = PROBESETID

    def translate2Probesetid(inverseDict: Map[Symbol, Probesetid], failover: String = "OOPS") = this

    /**
      * Translate a Probeset signature _back_ to a Symbol signature.
      *
      * @param inverseDict A dictionary (`Map`) `Symbol -> Probesetid`
      * @param failover    A placeholder for missing values, default is "OOPS"
      */
    def translate2Symbol(inverseDict: Map[Symbol, Probesetid], failover: String = "OOPS"): SymbolSignature = {
      val safeTranslation = safeTranslate2Symbol(inverseDict)
      SymbolSignature(safeTranslation.map(_.getOrElse(failover)))
    }

    /**
      * Translate a Probesetid signature to a Index signature.
      *
      * @param dict     A dictionary (`Map`) `Index -> Probesetid`
      * @param failover A placeholder for missing values, default is "0"
      */
    def translate2Index(dict: Map[Index, Probesetid], failover: String = "0"): IndexSignature = {
      val safeTranslation = safeTranslate2Index(dict)
      IndexSignature(safeTranslation.map(_.getOrElse(failover)))
    }

    /**
      * Translate a Probesetid signature _back to a Symbol signature. The result is wrapped in an `Option`.
      *
      * @param inverseDict A dictionary (`Map`) `Symbol -> Probesetid`
      */
    def safeTranslate2Symbol(inverseDict: Map[Symbol, Probesetid]): Array[Option[Symbol]] = {
      val dict = inverseDict.map(_.swap)
      signature.map { g =>
        val translation = dict.get(g.abs)
        translation.map(go => g.sign + go)
      }
    }

    /**
      * Translate a Probesetid signature to a Index signature. The result is wrapped in an `Option`.
      *
      * @param dict A dictionary (`Map`) `Index -> Probesetid`
      */
    def safeTranslate2Index(dict: Map[Index, Probesetid]): Array[Option[String]] = {
      val inverseDict = dict.map(_.swap)
      signature.map { g =>
        val translation = inverseDict.get(g.abs)
        translation.map(go => g.sign + go)
      }
    }

  }

  /**
    * Signature with index notation.
    */
  case class IndexSignature(val signature: SignatureType) extends Signature with Serializable {

    val notation: NotationType = INDEX

    /**
      * Translate a Index signature _back to a Probesetid signature. The result is wrapped in an `Option`.
      *
      * @param inverseDict A dictionary (`Map`) `Symbol -> Probesetid`
      * @todo Check This out!!!
      */
    def safeTranslate2Symbol(inverseDict: Map[Symbol, Probesetid]): Array[Option[Symbol]] = {
      val dict = inverseDict.map(_.swap)
      signature.map { g =>
        val translation = dict.get(g.abs)
        translation.map(go => g.sign + go)
      }
    }

    /**
      * Translate a Index  signature to a Probesetid signature.
      *
      * @param dict     A dictionary (`Map`) `Index -> Probesetid`
      * @param failover A placeholder for missing values, default is "OOPS"
      */
    def translate2Probesetid(dict: Map[Index, Probesetid], failover: String = "OOPS") = {
      val safeTranslation = safeTranslate2Probesetid(dict)
      ProbesetidSignature(safeTranslation.map(_.getOrElse(failover)))
    }

    /**
      * Translate a Index signature _back to a Probesetid signature. The result is wrapped in an `Option`.
      *
      * @param dict A dictionary (`Map`) `Index -> Probesetid`
      */
    def safeTranslate2Probesetid(dict: Map[Index, Probesetid]): Array[Option[Probesetid]] = {
      signature.map { g =>
        val translation = dict.get(g.abs.toInt)
        translation.map(go => g.sign + go)
      }
    }

    // No bullet-proof approach, should provide failsafe here as well
    val asSignedInt = signature.map(_.toDouble.toInt)

  }

}
