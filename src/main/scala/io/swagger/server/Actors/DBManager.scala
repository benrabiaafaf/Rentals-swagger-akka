package io.swagger.server.Actors

import java.io.{BufferedWriter, FileNotFoundException, FileWriter, IOException}
import akka.actor.{Actor, ActorLogging, Props}
import io.swagger.server.Actors.DBManager.{get_all_properties, get_property, post_comment_to_proprty, post_property}
import io.swagger.server.model.{Comment, Commented_property, Simple_property}
import spray.json.JsonParser.ParsingException


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
    var properties: List[Commented_property] = List()
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

  private def save_properties(properties: List[Commented_property]): Unit = {
    val buffer = new BufferedWriter(new FileWriter(properties_path))
    buffer.write(properties.toJson.toString)
    buffer.close
  }


  override def receive: Receive = {

    case post_comment_to_proprty(propertyId, comment) => sender() ! post_comment_to_proprty(propertyId, comment)
    case post_property(simple_property) => sender() ! post_property(simple_property)
    case get_all_properties() => sender() ! get_all_properties()
    case get_property(propertyId) => sender() ! get_property(propertyId)
  }

  def get_all_properties(): List[Simple_property] = {
    val commented_properties = read_properties()
    val simple_properties = commented_properties.map(_.property.get)
    return simple_properties
  }

  def get_property(propertyId: String): Option[Commented_property] = {
    val commented_properties = read_properties()
    val requested_property = commented_properties.find(item => item.property.get.property_id == propertyId)
    if (requested_property.getOrElse(None) != None) {
      return Some(requested_property.get)
    } else {
      return None
    }

  }

  def post_property(simple_property: Simple_property): Option[Simple_property] = {
    //TODO : [ Check weather property_id exists or not, make it return a simple property]
    val properties = read_properties()
    val exists = properties.exists(item => item.property.get.property_id == simple_property.property_id)
    if ( ! exists){
      val updated_properties = properties ++: List(
        new Commented_property(property = Some(simple_property),
          comments = Option(Nil)))
      save_properties(updated_properties)
      return Some(simple_property)
    }
    return None
  }

  def post_comment_to_proprty(propertyId: String, comment: Comment): Option[Comment] = {
    // read properties list
    val commented_properties = read_properties()
    // find property which id is propertyId
    val index = commented_properties.indexWhere(item => item.property.get.property_id == propertyId)
    // if found
    if (index != -1) {
      // get the property with it's comments
      val commented_property = commented_properties(index)
      // check weather the user has already posted a comment for the same year
      val exists = commented_property.comments.getOrElse(List.empty).exists(item => {
        item.user_id == comment.user_id && item.year == comment.year})
      if ( exists == false) {
          val updated_item = commented_property.copy(comments = Option(commented_property.comments.getOrElse(List.empty) ++: List(comment)))
          val updated_commented_properties = commented_properties.take(index) ++: List(updated_item) ++: commented_properties.takeRight(commented_properties.length - index - 1)
          save_properties(updated_commented_properties)
          return Some(comment)
      }
    }
    return None
  }
}
