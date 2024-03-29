package com.dataintuitive.luciuscore
package signatures

import genes.GenesDB

sealed trait Signature[A] extends Serializable {

    val signature: Array[A]
    val notation = this.getClass

    override def toString = signature.mkString(s"Signature of type ${notation}: [", ",", "]")

    def toArray = signature.map(_.toString)
}

object Signatures extends SignaturesTrait

trait SignaturesTrait extends Serializable {

  case class SymbolSignature(signature: Array[SignedSymbol]) extends Signature[SignedSymbol] {

      def this(signature: Array[String]) { this(signature.map(g => SignedString(g))) }

      def toIdSignature(implicit genes: GenesDB, failover: String = "OOPS"):IdSignature = {
          val dict = genes.symbol2idDict
          val translated = signature.map { g =>
              val translation = dict.get(g.abs)
              translation.map(ug => SignedString(g.sign, ug)).getOrElse(SignedString(failover))
          }
          IdSignature(translated)
      }

      def toIndexSignature(implicit genes: GenesDB, failover: Int = 0):IndexSignature =
          toIdSignature.toIndexSignature

  }

  case class IdSignature(signature: Array[SignedId]) extends Signature[SignedId] {

      def this(signature: Array[String]) { this(signature.map(g => SignedString(g))) }

      def toSymbolSignature(implicit genes: GenesDB, failover: String = "OOPS"):SymbolSignature = {
          // TODO : inverse dict should be handled by Genes, not here!
          val dict = for ((k,v) <- genes.symbol2idDict) yield (v, k)
          val translated = signature.map { g =>
              val translation = dict.get(g.abs)
              translation.map(ug => SignedString(g.sign, ug)).getOrElse(SignedString(failover))
          }
          SymbolSignature(translated)

      }

      def toIndexSignature(implicit genes: GenesDB, failover: Int = 0):IndexSignature = {
          val dict = for ((k,v) <- genes.index2idDict) yield (v, k)
          val translated = signature.map { g =>
              val translation = dict.get(g.abs)
              translation.map(ui => SignedInt(g.sign, ui)).getOrElse(SignedInt(failover))
          }
          IndexSignature(translated)

      }

  }

  case class IndexSignature(signature: Array[SignedInt]) extends Signature[SignedInt]{

    def this(signature: Array[Int]) { this(signature.map(g => SignedInt(g))) }

    def toIdSignature(implicit genes: GenesDB, failover: String = "OOPS"):IdSignature = {
        val dict = genes.index2idDict
        val translated = signature.map { g =>
            val translation = dict.get(g.abs)
            translation.map(go => SignedString(g.sign, go)).getOrElse(SignedString(failover))
        }
        IdSignature(translated)
    }

    def toSymbolSignature(implicit genes: GenesDB, failover: Int = 0):SymbolSignature = 
        toIdSignature.toSymbolSignature

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
}
