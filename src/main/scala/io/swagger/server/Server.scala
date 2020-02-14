package io.swagger.server

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.marshalling.ToEntityMarshaller
import akka.http.scaladsl.server.Route
import io.swagger.server.api.{DefaultApiMarshaller, DefaultApiService}
import io.swagger.server.model.{Comment, Commented_property, Simple_property}
import spray.json.{JsonFormat, RootJsonFormat}
import spray.json.DefaultJsonProtocol._
import spray.json.JsObject._


object ApiService extends DefaultApiService {
  /**
    * Code: 200, Message: a list of availble properties, DataType: List[simple_property]
    */
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

object ApiMarshaller extends DefaultApiMarshaller with SprayJsonSupport {

  override implicit def toEntityMarshallersimple_propertyarray: ToEntityMarshaller[List[Simple_property]] = listFormat(jsonFormat8(Simple_property))

  override implicit def toEntityMarshallersimple_property: ToEntityMarshaller[Simple_property] = jsonFormat8(Simple_property)

  override implicit def toEntityMarshallercommented_property: ToEntityMarshaller[Commented_property] = jsonFormat2(Commented_property)

  override implicit def toEntityMarshallercomment: ToEntityMarshaller[Comment] = jsonFormat4(Comment)
}

object Server extends App {

}
