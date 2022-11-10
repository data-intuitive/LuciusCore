package com.dataintuitive.luciuscore.io

case class DatedObject[T](date: java.time.LocalDate, obj: T)
case class DatedVersionedObject[T](date: java.time.LocalDate, version: Version, obj: T)

case class State[T](state: List[DatedVersionedObject[T]] = List()) {
  def lastVersion = 
    if (state.length == 0)
      Version(-1,0)
    else
      Version(state.sortBy(_.version).last.version.major, state.sortBy(_.version).last.version.minor)

  def lastDate =
    if (state.length == 0)
      java.time.LocalDate.MIN
    else
      state.sortBy(_.version).last.date

  def +(el: DatedVersionedObject[T]):State[T] = State(el :: state)
}
