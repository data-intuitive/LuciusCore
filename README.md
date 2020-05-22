![Scala CI](https://github.com/data-intuitive/LuciusCore/workflows/Scala%20CI/badge.svg)

# LuciusCore

This is the core library containing the domain logic for the __ComPass__ application. For more information, please refer to the repository for the [web interface](https://github.com/data-intuitive/LuciusWeb).

## Data model (v2)

A schematic overview of the model. Field access is best performed by means of the available `Lens`'s.

```
DbRow
  id                     Option[String]
  sampleAnnotations
    sample
      id                 Option[String]
      batch              Option[String]
      plateid            Option[String]
      well               Option[String]
      protocolname       Option[String]
      concentration      Option[String]
      year               Option[String]
      time               Option[String]
    t                    Option[Array[Double]]
    p                    Option[Array[Double]]
    r                    Option[Array[Double]]
  compoundAnnotations
    compound
      jnjs               Option[String]
      jnjb               Option[String]
      smiles             Option[String]
      inchikey           Option[String]
      name               Option[String]
      ctype              Option[String]
    knownTargets         Option[Seq[Gene]]
    predictedTargets     Option[Seq[Gene]]
  filters                Seq[Filter]
```



