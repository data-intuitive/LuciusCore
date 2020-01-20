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

    def toIndexSignature(implicit genes: GeneDB, failover: Int = 0):IndexSignature =
        toProbesetidSignature.toIndexSignature

}

case class ProbesetidSignature(signature: Array[SignedProbesetid]) extends Signature[SignedProbesetid] {

    def this(signature: Array[String]) { this(signature.map(g => SignedString(g))) }

    def toIndexSignature(implicit genes: GeneDB, failover: Int = 0):IndexSignature = {
        val dict = for ((k,v) <- genes.index2ProbesetidDict) yield (v, k)
        val translated = signature.map { g =>
            val translation = dict.get(g.abs)
            translation.map(ui => SignedInt(g.sign, ui)).getOrElse(SignedInt(failover))
        }
        IndexSignature(translated)

    }

}

case class IndexSignature(signature: Array[SignedInt]) extends Signature[SignedInt]{

  def this(signature: Array[Int]) { this(signature.map(g => SignedInt(g))) }

  def toSymbolSignature(implicit genes: GeneDB, failover: Int = 0):SymbolSignature =
      toProbesetidSignature.toSymbolSignature

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
