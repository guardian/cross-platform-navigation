package com.gu.navigation.model

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.json.Writes._
import play.api.libs.functional.syntax._

case class NavigationSection(
  title: String,
  path: String,
  mobileOverride: Option[Boolean] = None,
  sections: Option[List[NavigationSection]] = None
)

object NavigationSection  {


  implicit val navigationSectionReads: Reads[NavigationSection] = (
    (__ \ "title").read[String] and
      (__ \ "path").read[String] and
      (__ \ "mobileOverride").readNullable[Boolean] and
      (__ \ "sections").lazyReadNullable(implicitly[Reads[List[NavigationSection]]])
    )(NavigationSection.apply _)

  implicit val navigationSectionWrites: Writes[NavigationSection] = (
    (__ \ "title").write[String] and
      (__ \ "path").write[String] and
      (__ \ "mobileOverride").writeNullable[Boolean] and
      (__ \ "sections").lazyWriteNullable(implicitly[Writes[List[NavigationSection]]])
    )(unlift(NavigationSection.unapply _))

  implicit lazy val disNavigationSectionFormat: Format[NavigationSection] = Format(navigationSectionReads, navigationSectionWrites)

}

object Navigation {
  implicit val jf = Json.format[Navigation]
}

case class Navigation(pillars: List[NavigationSection])
