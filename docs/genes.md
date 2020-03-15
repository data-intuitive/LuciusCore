# Genes and Signatures API

Load a genes/probesets file:

```scala
import com.dataintuitive.luciuscore.io._

val base = "<location_base>"
val geneAnnotationsFilepath = base + "L1000/featureData.txt"
val genes = GenesIO.loadGenesFromFile(sc, geneAnnotationsFilepath)
```

Convert to a `GenesDB` instance:

```scala
import com.dataintuitive.luciuscore.genes._

implicit val genesDB = new GenesDB(genes)
```

Please note the `implicit` here, as it makes our lives easier down the road.

Now, create a signature of type `SymbolSignature`:

```scala
val ss = new SymbolSignature(Array("-MELK", "BRCA1"))
```

This can be converted (using the implicit genesDB instance above) to any other representation of the signature:

```scala
ss.toIndexSignaure
ss.toProbesetidSignature
```

In some cases, one just wants to export a signature to something easily interpretable. In order to convert a signature to an `Array[String]` instead of the model used internally, one can use this approach:

```scala
val sSignature = new SymbolSignature("HSPA1A DNAJB1 DDIT4 -TSEN2".split(" "))
  // -> SymbolSignature: [HSPA1A,DNAJB1,DDIT4,-TSEN2]
sSignature.toArray
  // -> Array(HSPA1A, DNAJB1, DDIT4, -TSEN2)
```

The latter is used in LuciusAPI, for instance.

