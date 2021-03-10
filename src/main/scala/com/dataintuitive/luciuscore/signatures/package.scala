package com.dataintuitive.luciuscore

import com.dataintuitive.luciuscore.genes._

package object signatures extends SignaturesTrait {

  type GeneVector = Array[GeneType]

  object Sign extends Enumeration {
        type Sign = Value
        val PLUS, MINUS = Value
    }

  import Sign._

  // Concrete types
  type SignedSymbol = SignedString
  type SignedProbesetid = SignedString
  type SignedIndex = SignedInt

}
