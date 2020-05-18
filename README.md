![Scala CI](https://github.com/data-intuitive/LuciusCore/workflows/Scala%20CI/badge.svg)

# LuciusCore

## Data model

```
DbRow
  pwid
  sampleAnnotations
    sample
      pwid
      batch
      plateid
      well
      protocolname
      concentration
      year
    t
    p
    r
  compoundAnnotations
    compound
      jnjs
      jnjb
      smiles
      inchikey
      name
      ctype
    knownTargets
    predictedTargets

```

