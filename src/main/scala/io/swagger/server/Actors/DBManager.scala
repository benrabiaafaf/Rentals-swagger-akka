package io.swagger.server.Actors


import java.io.{BufferedWriter, FileNotFoundException, FileWriter, IOException}

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import io.swagger.server.Actors.DBManager.{get_all_properties, get_property, post_comment_to_proprty, post_property}
import io.swagger.server.model.{Comment, Commented_property, Simple_property}
import spray.json.JsonParser.ParsingException
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

    case post_comment_to_proprty(propertyId, comment) => ???
    case post_property(simple_property) => ???
    case get_all_properties() => sender() ! get_all_properties()
    case get_property(propertyId) => sender()! get_property(propertyId)
  }

  def get_all_properties(): List[Simple_property] = {
    val commented_properties = read_properties()
    val simple_propertie = commented_properties.map(_.property)
    return simple_propertie
  }

  def get_property(propertyId: String): Commented_property  ={
    val commented_properties = read_properties()
    val requested_property = commented_properties.find( item => item.property.property_id == propertyId)
    return requested_property.get
  }
}

object Main {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem()
    val dBManager = system.actorOf(DBManager())
    dBManager ! "test"
  }
}
