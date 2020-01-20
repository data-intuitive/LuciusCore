package com.dataintuitive.luciuscore.signatures

import com.dataintuitive.luciuscore.Model._
import com.dataintuitive.luciuscore.genes.GeneDB

sealed trait Signature[A] extends Serializable {

    val signature: Array[A]
    val notation = this.getClass

    override def toString = signature.mkString(s"Signature of type ${notation}: [", ",", "]")
}

case class SymbolSignature(signature: Array[SignedSymbol]) extends Signature[SignedSymbol] {

    def this(signature: Array[String]) { this(signature.map(g => SignedString(g))) }

    def toIndexSignature(implicit geneDB: GeneDB, failover: Int = 0):IndexSignature =
      IndexSignature(
        signature.map{ symbol =>
          val indexOption = geneDB.lookup(symbol.abs)
          indexOption.map(i => SignedInt(symbol.sign, i)).getOrElse(SignedInt(failover))
        }
      )

}

case class IndexSignature(signature: Array[SignedInt]) extends Signature[SignedInt]{

  def this(signature: Array[Int]) { this(signature.map(g => SignedInt(g))) }

  def toSymbolSignature(implicit geneDB: GeneDB, failover: String = "OOPS"):SymbolSignature =
      SymbolSignature(
        signature.map{ index =>
          val symbolOption = geneDB.lookup(index.abs)
          symbolOption.map(symbol => SignedString(index.sign, symbol)).getOrElse(SignedString(failover))
        }
      )

  /**
    * Convert an index-based signature to an ordered rank vector.
    * Remark 1: We need to provide the length of the resulting RankVector.
    * Remark 2: Indices are 1-based.
    */
  def toOrderedRankVector(length: Int): RankVector = {
    val sLength = signature.length
    val ranks = (sLength to 1 by -1).map(_.toDouble)
    val unsignedRanks = signature zip ranks
    val signedRanks = unsignedRanks
      .map { case (signedInt, unsignedRank) =>
        (signedInt.abs, (signedInt.signInt * unsignedRank).toDouble)
      }.toMap
    val asSeq = for (el <- 1 to length by 1) yield signedRanks.getOrElse(el, 0.0)
    asSeq.toArray
  }

  /**
    * Convert an index-based signature to an unordered rank vector.
    * Remark 1: We need to provide the length of the resulting RankVector.
    * Remark 2: Indices are 1-based.
    */
  def toUnorderedRankVector(length: Int): RankVector = {
    val sLength = signature.length
    val ranks = (sLength to 1 by -1).map(_ => 1.0)  // This is the only difference with the above, all ranks are 1
    val unsignedRanks = signature zip ranks
    val signedRanks = unsignedRanks
      .map { case (signedInt, unsignedRank) =>
        (signedInt.abs, (signedInt.signInt * unsignedRank).toDouble)
      }.toMap
    val asSeq = for (el <- 1 to length by 1) yield signedRanks.getOrElse(el, 0.0)
    asSeq.toArray
  }

}
