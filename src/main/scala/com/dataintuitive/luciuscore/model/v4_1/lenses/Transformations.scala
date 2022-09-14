package com.dataintuitive.luciuscore
package model.v4_1
package lenses

import scalaz.Lens

import CombinedPerturbationLenses.profileLens
import CombinedPerturbationLenses.lengthLens
import CombinedPerturbationLenses.pTypeLens
import model.v4.Profile

object Transformations extends Serializable {

    val ranksT = profileLens =>= {
        (x:Profile) => (x.t, x.p) match {
            case (Some(t), Some(p)) => x.copy(
                r = Some(TransformationFunctions.stats2RankVector(t, p))
              )
            case _ => x
        }
    }

    val lengthT = (l:Int) => lengthLens =>= { (_:Int) => l }
    val pTypeT = (t:String) => pTypeLens =>= { (_:String) => t }

}
