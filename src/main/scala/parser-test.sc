import com.gu.navigation.model.NavigationSection
import play.api.libs.json.{JsError, JsSuccess, Json}

import scala.io.Source

def getIt(s: String): String = { s.reverse }

val path = "json/single-section.json"



val f = s"${System.getProperty("user.home")}/working/cross-platform-navigation/${path}"
val json = Some(Source.fromFile(f).getLines().mkString)
/*
val json = Json.parse(d)
val sections = Json.fromJson[NavigationSection](json) match {
  case JsSuccess(value, _) => value
  case JsError(_) => NavigationSection("","",false)
}
*
def printSection(section: NavigationSection, space: String = "") : Unit = {

  println(s"${space}Title: ${section.title}")
  println(s"${space}Path: ${section.path}")
  println(s"${space}Edition: ${section.path}")

  section.sections match {
    case Some(sections) =>
      println(s"${space}[")
      sections.foreach(s => printSection(s, s"  $space"))
      println(s"${space}]")
    case None =>
  }
}






