package io.swagger.server.Actors


import java.io.{BufferedWriter, FileNotFoundException, FileWriter, IOException}

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import io.swagger.server.Actors.DBManager.{get_all_properties, get_property, post_comment_to_proprty, post_property}
import io.swagger.server.model.{Comment, Commented_property, Simple_property}
import spray.json.JsonParser.ParsingException

import scala.Option
import scala.math.BigDecimal


object DBManager {

  def apply(): Props = Props(new DBManager())

  case class post_comment_to_proprty(propertyId: String, comment: Comment)

  case class post_property(property: Simple_property)

  case class get_all_properties()

  case class get_property(propertyId: String)

}

class DBManager extends Actor with ActorLogging {

  import io.swagger.server.CostumJsonProtocol._
  import spray.json._

  val properties_path = "src/main/scala/io/swagger/server/data/properties.json"

  private def read_properties(): List[Commented_property] = {
    var properties : List[Commented_property] = List()
    try {
      properties = scala.io.Source.fromFile(properties_path)("UTF-8").mkString.parseJson.convertTo[List[Commented_property]]
    } catch {
      case err: FileNotFoundException => {
        log.info("Exception: File missing")
      }
      case err: IOException => {
        log.info("Input/output Exception")
      }
      case err: ParsingException => ???
    }
    return properties

  }

  private def save_properties(properties : List[Commented_property]): Unit = {
    val buffer = new BufferedWriter(new FileWriter(properties_path))
    buffer.write(properties.toJson.toString)
    buffer.close
  }


  override def receive: Receive = {

    case post_comment_to_proprty(propertyId, comment) => post_comment_to_proprty(propertyId,comment)
    case post_property(simple_property) => post_property(simple_property)
    case get_all_properties() => sender() ! get_all_properties()
    case get_property(propertyId) => sender()! get_property(propertyId).getOrElse(None)
  }

  def get_all_properties(): List[Simple_property] = {
    val commented_properties = read_properties()
    val simple_properties = commented_properties.map(_.property)
    log.info(simple_properties.toString)
    return simple_properties
  }

  def get_property(propertyId: String): Option[Commented_property]  ={
    val commented_properties = read_properties()
    val requested_property = commented_properties.find( item => item.property.property_id == propertyId)
    log.info(requested_property.getOrElse(None).toString)
    return requested_property
  }

  def post_property(simple_property: Simple_property)={
    //TODO : Check weather property_id exists or not
    val properties = read_properties()
    val updated_properties = properties ++: List(new Commented_property(property = simple_property, comments = Option(Nil)))
    log.info(updated_properties.toString)
    save_properties(updated_properties)
  }

  def post_comment_to_proprty(propertyId : String, comment: Comment): Unit ={
    //TODO : check weather user has already posted a comment for the specified year
    val commented_properties = read_properties()
    val index = commented_properties.indexWhere( item => item.property.property_id == propertyId)
    if ( index != -1){
      val item = commented_properties(index)
      val updated_item = item.copy(comments = Option(item.comments.getOrElse(Nil) ++: List(comment)))
      val updated_commented_properties = commented_properties.take(index) ++: List(updated_item) ++: commented_properties.takeRight(commented_properties.length-index-1)
      log.info(updated_commented_properties.toString)
      save_properties(updated_commented_properties)
    }
  }
}
