package com.dataintuitive.luciuscore.api

case class FlatDbRow(
  id: String,
  cell: String,
  dose: String,
  treatmentType:String,
  treatmentId: String,
  informative: Boolean
)
