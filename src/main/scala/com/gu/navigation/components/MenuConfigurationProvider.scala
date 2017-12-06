package com.gu.navigation.components

import com.amazonaws.auth.{AWSCredentialsProvider, DefaultAWSCredentialsProviderChain}
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.gu.navigation.Edition
import com.gu.navigation.components.LocalFilesystemMenuConfigurationProvider.readResource
import com.gu.navigation.model.Navigation
import play.api.libs.json._
import play.api.libs.json.Writes._
import play.api.libs.json.Reads._

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source

trait MenuConfigurationProvider {

  //def readResource(path: String) : Option[String]

  def getMenu(edition: Edition)(implicit ec: ExecutionContext) : Future[Option[Navigation]] = Future {
    val localEdition = s"${edition.name}.json"
    readResource(edition.name).flatMap {
      maybeJase => Json.fromJson[Navigation](Json.parse(maybeJase)) match {
        case JsSuccess(navigation, _ ) => Some(navigation)
        case JsError(errors) =>
          println(s"Need to log these errors to a logger here: $errors")
          None
      }
    }
  }
}

object LocalFilesystemMenuConfigurationProvider extends MenuConfigurationProvider {

  def readResource(path: String): Option[String] =
    Option(getClass.getClassLoader.getResource(path)).map(Source.fromURL(_).mkString)

}

trait S3MenuConfigurationProvider extends MenuConfigurationProvider {

  def bucket: String
  //Todo these will need to vary depending on whether we're mapi or frontend. Get them via tags
  def credentials: DefaultAWSCredentialsProviderChain

  //TODO mask strings in sealed trait
  private def fromS3(path: String) : Option[String] = {
    val s3Client = {
      val builder = AmazonS3ClientBuilder.standard()
      builder.setCredentials(credentials)
      builder.build()
    }

    //TODO log summat

    val s3Object = s3Client.getObject(bucket, path)
    Some(Source.fromInputStream(s3Object.getObjectContent).mkString)
  }

  //TODO rename
  def readResource(path: String): Option[String] = fromS3(path)

}

