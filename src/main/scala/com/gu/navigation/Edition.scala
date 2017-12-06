package com.gu.navigation

sealed trait Edition {
  def code: String
  def name: String
}

object Edition {

  private val all: Seq[Edition] = Seq(UK, US, AU, International)

  private val editions: Map[String, Edition] =  Map(
    UK.toString -> UK,
    US.toString -> US,
    AU.toString -> AU,
    International.toString -> International
  )

  private val editionFromName = Edition.all.map(ed => ed.name -> ed)

}

case object UK extends Edition {
  override def toString: String = "uk"
  override def name: String = "United Kingdon"
  override def code: String = "uk"
}

case object US extends Edition {
  override def toString: String = "us"
  override def code: String = "us"
  override def name: String = "United Kingdon"
}

case object AU extends Edition {
  override def toString: String = "us"
  override def code: String = "us"
  override def name: String = "United Kingdon"
}

case object International extends Edition {
  override def toString: String = "us"
  override def code: String = "us"
  override def name: String = "United Kingdon"
}
