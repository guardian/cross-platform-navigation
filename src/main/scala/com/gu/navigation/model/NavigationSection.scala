package com.gu.navigation.model

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.json.Writes._
import play.api.libs.functional.syntax._

case class NavigationSection(
  title: String,
  path: String,
  mobileOverride: Option[String] = None,
  sections: Option[List[NavigationSection]] = None,
  editionOverride: Option[String] = None
)

object NavigationSection  {

  implicit val navigationSectionReads: Reads[NavigationSection] = (
    (__ \ "title").read[String] and
      (__ \ "path").read[String] and
      (__ \ "mobileOverride").readNullable[String] and
      (__ \ "sections").lazyReadNullable(implicitly[Reads[List[NavigationSection]]]) and
      (__ \ "editionOverride").readNullable[String]
    )(NavigationSection.apply _)

  implicit val navigationSectionWrites: Writes[NavigationSection] = (
    (__ \ "title").write[String] and
      (__ \ "path").write[String] and
      (__ \ "mobileOverride").writeNullable[String] and
      (__ \ "sections").lazyWriteNullable(implicitly[Writes[List[NavigationSection]]]) and
      (__ \ "editionOverride").writeNullable[String]
    )(unlift((ns: NavigationSection) => Some((ns.title, ns.path, ns.mobileOverride, ns.sections, ns.editionOverride))))

  implicit lazy val disNavigationSectionFormat: Format[NavigationSection] = Format(navigationSectionReads, navigationSectionWrites)

}

object Navigation {
  implicit val jf: Format[Navigation] = Json.format[Navigation]
}

case class Navigation(pillars: List[NavigationSection])
