package com.gu.navigation.model

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.json.Writes._
import play.api.libs.functional.syntax._

case class NavigationSection(
   title: String,
   path: String,
   editionalised: Boolean,
   mobileOverride: Option[Boolean] = None,
   sections: Option[List[NavigationSection]] = None
)

object NavigationSection  {

  implicit lazy val navigationSectionReads: Reads[NavigationSection] = (
    (__ \ "title").read[String] and
      (__ \ "path").read[String] and
      (__ \ "editionalised").read[Boolean] and
      (__ \ "mobileOverride").readNullable[Boolean] and
      (__ \ "sections").lazyReadNullable(implicitly[Reads[List[NavigationSection]]])
    )(NavigationSection.apply _)
}


