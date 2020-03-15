[![Build
Status](https://travis-ci.org/data-intuitive/LuciusCore.svg?branch=master)](https://travis-ci.org/data-intuitive/LuciusCore)

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

