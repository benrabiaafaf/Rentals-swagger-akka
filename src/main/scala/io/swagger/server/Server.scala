package io.swagger.server

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.marshalling.ToEntityMarshaller
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.unmarshalling.FromRequestUnmarshaller
import io.swagger.server.CostumJsonProtocol.jsonFormat8
import io.swagger.server.api._
import io.swagger.server.model._
import spray.json.{DefaultJsonProtocol, RootJsonFormat}
import spray.json.DefaultJsonProtocol.{jsonFormat4, jsonFormat8, _}


object ApiService extends DefaultApiService {

  /**
    * Code: 200, Message: a list of availble properties, DataType: List[simple_property]
    */

  //  type Route = RequestContext => Future[RouteResult]
  override def propertiesGet()(implicit toEntityMarshallersimple_propertyarray: ToEntityMarshaller[List[Simple_property]]): Route = ???

  /**
    * Code: 200, Message: add the given property, DataType: simple_property
    */
  override def propertiesPost(body: Simple_property)(implicit toEntityMarshallersimple_property: ToEntityMarshaller[Simple_property]): Route = ???

  /**
    * Code: 200, Message: get the specified property information with comments, DataType: commented_property
    */
  override def propertiesPropertyIdGet(propertyId: String)(implicit toEntityMarshallercommented_property: ToEntityMarshaller[Commented_property]): Route = ???

  /**
    * Code: 200, Message: add the given coomment to the specified property, DataType: comment
    */
  override def propertiesPropertyIdPost(body: Comment, propertyId: String)(implicit toEntityMarshallercomment: ToEntityMarshaller[Comment]): Route = ???
}

object CostumJsonProtocol extends DefaultJsonProtocol {
  implicit val simple_propertyFormat = jsonFormat8(Simple_property)
  implicit val commentFormat = jsonFormat4(Comment)
  implicit val commeented_propertyFormat = jsonFormat2(Commented_property)
}

object ApiMarshaller extends DefaultApiMarshaller with SprayJsonSupport {

  import CostumJsonProtocol._

  implicit val toEntityMarshallercomment: ToEntityMarshaller[Comment] = commentFormat

  implicit val toEntityMarshallersimple_property: ToEntityMarshaller[Simple_property] = simple_propertyFormat

  implicit val toEntityMarshallercommented_property: ToEntityMarshaller[Commented_property] = commeented_propertyFormat

  implicit val toEntityMarshallersimple_propertyarray: ToEntityMarshaller[List[Simple_property]] = listFormat(simple_propertyFormat)


}

object Server extends App {

}
