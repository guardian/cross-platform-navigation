import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.json.Writes._
import play.api.libs.functional.syntax._

import scala.io.Source

val path = "json/single-pillar.json"

case class NavigationSection(
                              title: String,
                              path: String,
                              editionalised: Boolean,
                              mobileOverride: Option[Boolean] = None,
                              sections: Option[List[NavigationSection]] = None
                            )

object NavigationSection  {

  //implicit val jf = Json.format[NavigationSection]


  implicit val navigationSectionReads: Reads[NavigationSection] = (
    (__ \ "title").read[String] and
      (__ \ "path").read[String] and
      (__ \ "editionalised").read[Boolean] and
      (__ \ "mobileOverride").readNullable[Boolean] and
      (__ \ "sections").lazyReadNullable(implicitly[Reads[List[NavigationSection]]])
    )(NavigationSection.apply _)

  implicit val navigationSectionWrites: Writes[NavigationSection] = (
    (__ \ "title").write[String] and
      (__ \ "path").write[String] and
      (__ \ "editionalised").write[Boolean] and
      (__ \ "mobileOverride").writeNullable[Boolean] and
      (__ \ "sections").lazyWriteNullable(implicitly[Writes[List[NavigationSection]]])
    )(unlift(NavigationSection.unapply _))


  /*
    implicit lazy val navigationSectionFormat: Format[NavigationSection] = (
      (__ \ "title").format[String] and
      (__ \ "path").format[String] and
      (__ \ "editionalised").format[Boolean] and
8     (__ \ "mobileOverride").formatNullable[Boolean] and
      (__ \ "sections").lazyFormatNullable(implicitly[Format[List[NavigationSection]]])
    )(NavigationSection.apply _, unlift(NavigationSection.unapply _))

  */
  implicit lazy val disNavigationSectionFormat: Format[NavigationSection] = Format(navigationSectionReads, navigationSectionWrites)

}

object Navigation {
  implicit val jf = Json.format[Navigation]
}

case class Navigation(pillars: List[NavigationSection])

def slurp(path: String) : Option[String]  = {
  val f = s"${System.getProperty("user.home")}/working/cross-platform-navigation/${path}"
  Some(Source.fromFile(f).getLines().mkString)
}

slurp(path).flatMap { maybeJson =>
  Json.fromJson[Navigation](Json.parse(maybeJson)) match {
    case JsSuccess(nav, _) => Some(nav)
    case JsError(errors) =>
      println(s"does not parse: $errors")
      None
  }

}







/*
val json = Json.parse(d)
Json.fromJson[Navigation](json)
*/

