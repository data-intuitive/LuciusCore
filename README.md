![Scala CI](https://github.com/data-intuitive/LuciusCore/workflows/Scala%20CI/badge.svg)

# LuciusCore

This is the core library containing the domain logic for the __ComPass__ application. For more information, please refer to the repository for the [web interface](https://github.com/data-intuitive/LuciusWeb).

## Version 4

For version 4, we wanted to included different types of [perturbagens](https://clue.io/connectopedia/perturbagen_types_and_controls). Similar to the earlier versions we want to be able to express the data in a typesafe model in the Scala world. While this can be achieved in many ways using the features available in Scala for typing, compatibility with the Parquet storage format and especially Spark's ability to load and query it are crucial.

After extensive experimentation, we found out the best way to model different _subtypes_ in Scala in line with Parquet is to create a union type that combines all subtypes into one supertype. Therefore the `Treatment` type contains different _slots_ that are just different types of _treatment_. There is also a `generic` slot that is available for convenience purposes.

We refer to the tests for examples on how to use the data model for loading.

Accessing the fields in the model is best achieved using the available lenses. Combined lenses are available so that deep access can easily be performed.

A schematic overview of the model:

```
Perturbation
  id
  info                    information about the experiment
    cell
    batch
    plate
    well
    year
    extra
  profiles                a list of profiles for future compabiblity
    pType
    length
    t
    p
    r
    logFc
  trtType                 trt_cp / trt_lig / ...
  trt                     treatment information
    trt_generic           a generic representation
    trt_cp                slot for trt_cp type perturbagens
    trt_lig               slot for trt_lig type perturbagens
    trt_sh                ...
    ctl_vector            ...
  filters
```

## Version 3

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



