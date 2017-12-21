package com.gu.navigation

import com.gu.navigation.model.Navigation
import org.slf4j.LoggerFactory
import play.api.libs.json.{JsError, JsSuccess, Json}

case class NavigationParseError(message: String) extends Exception(message)

trait NavigationProvider {
     def navigation(navAsJson: String) : Navigation
}

class GarnettNavigationProvider extends NavigationProvider {

  private val logger = LoggerFactory.getLogger(this.getClass)

  override def navigation(navAsJson: String) = Json.fromJson[Navigation](Json.parse(navAsJson)) match {
    case JsSuccess(navigation, _) => navigation
    case JsError(errors) =>
      logger.error(s"Could not extract navigation from json. Errors: $errors ")
      val errorPaths = errors map { error => error._1.toString()} mkString(",")
      throw new NavigationParseError(s"Could not extract navigation: Error path(s) $errorPaths")
  }
}


