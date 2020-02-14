package io.swagger.server.Actors

import akka.actor.{Actor, Props}
import io.swagger.server.model.{Comment, Commented_property, Simple_property}
import math.BigDecimal



object DBManager{
  def apply: Props = Props(new DBManager())

  case class post_comment_to_proprty(propertyId: String, comment: Comment)
  case class post_property(property: Simple_property)
  case class get_all_properties()
  case class get_property(propertyId: String)
}
class DBManager extends Actor{
  import DBManager._

  var properties : List[Commented_property]

  override def preStart(): Unit = {
    properties = List(
      new Commented_property(
        new Simple_property(
          property_id = Some("p1"),
          owner_id = Some("o1"),
          property_type = Some("T1"),
          adress = Some("Villetaneuse"),
          rooms = Some(1),
          area = Option(BigDecimal(20)),
          rent = Option(BigDecimal(600)),
          images = None
        ),
        comments = None)
    )
  }

  override def receive: Receive = {

    case post_comment_to_proprty(propertyId,comment) =>{
      val item = properties filter(item => item.property.property_id == propertyId)
      val index = properties.indexOf(item)

    }
    case post_property(simple_property) => ???
    case get_all_properties() => ???
    case get_property(propertyId) => ???
  }
}

object main{
  def main(args: Array[String]): Unit = {
    val properties = List(
      new Commented_property(
        new Simple_property(
          property_id = Some("p1"),
          owner_id = Some("o1"),
          property_type = Some("T1"),
          adress = Some("Villetaneuse"),
          rooms = Some(1),
          area = Option(BigDecimal(20)),
          rent = Option(BigDecimal(600)),
          images = None
        ),
        comments = None)
    )
    val propertyId = "p1"
    val item = properties filter(item => item.property.property_id == propertyId)
    val index = properties.indexOf(item)
    println(index)
  }
}