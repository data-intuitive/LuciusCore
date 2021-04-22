package com.dataintuitive.luciuscore.api

case class FlatDbRow(
  id: String,
  protocol: String,
  concentration: String,
  compoundType:String,
  compoundId: String,
  informative: Boolean
)
