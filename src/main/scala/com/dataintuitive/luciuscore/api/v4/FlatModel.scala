package com.dataintuitive.luciuscore.api.v4

case class FlatDbRow(
  id: String,
  cell: String,
  dose: String,
  treatmentType:String,
  treatmentId: String,
  informative: Boolean
)
