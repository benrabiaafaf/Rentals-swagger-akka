package io.swagger.server.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.unmarshalling.FromRequestUnmarshaller
import akka.http.scaladsl.marshalling.ToEntityMarshaller
import io.swagger.server.AkkaHttpHelper._
import io.swagger.server.model.Comment
import io.swagger.server.model.Simple_property
import io.swagger.server.model.comment
import io.swagger.server.model.commented_property
import io.swagger.server.model.simple_property

class DefaultApi(
    defaultService: DefaultApiService,
    defaultMarshaller: DefaultApiMarshaller
) {
  import defaultMarshaller._

  lazy val route: Route =
    path("properties") { 
      get {
        
          
            
              
                
                  defaultService.propertiesGet()
               
             
           
         
       
      }
    } ~
    path("properties") { 
      post {
        
          
            
              
                entity(as[Simple_property]){ body =>
                  defaultService.propertiesPost(body = body)
                }
             
           
         
       
      }
    } ~
    path("properties") { (propertyId) => 
      get {
        
          
            
              
                
                  defaultService.propertiesPropertyIdGet(propertyId = propertyId)
               
             
           
         
       
      }
    } ~
    path("properties") { (propertyId) => 
      post {
        
          
            
              
                entity(as[Comment]){ body =>
                  defaultService.propertiesPropertyIdPost(body = body, propertyId = propertyId)
                }
             
           
         
       
      }
    }
}

trait DefaultApiService {

  def propertiesGet200(responsesimple_propertyarray: List[simple_property])(implicit toEntityMarshallersimple_propertyarray: ToEntityMarshaller[List[simple_property]]): Route =
    complete((200, responsesimple_propertyarray))
  /**
   * Code: 200, Message: a list of availble properties, DataType: List[simple_property]
   */
  def propertiesGet()
      (implicit toEntityMarshallersimple_propertyarray: ToEntityMarshaller[List[simple_property]]): Route

  def propertiesPost200(responsesimple_property: simple_property)(implicit toEntityMarshallersimple_property: ToEntityMarshaller[simple_property]): Route =
    complete((200, responsesimple_property))
  /**
   * Code: 200, Message: add the given property, DataType: simple_property
   */
  def propertiesPost(body: Simple_property)
      (implicit toEntityMarshallersimple_property: ToEntityMarshaller[simple_property]): Route

  def propertiesPropertyIdGet200(responsecommented_property: commented_property)(implicit toEntityMarshallercommented_property: ToEntityMarshaller[commented_property]): Route =
    complete((200, responsecommented_property))
  /**
   * Code: 200, Message: get the specified property information with comments, DataType: commented_property
   */
  def propertiesPropertyIdGet(propertyId: String)
      (implicit toEntityMarshallercommented_property: ToEntityMarshaller[commented_property]): Route

  def propertiesPropertyIdPost200(responsecomment: comment)(implicit toEntityMarshallercomment: ToEntityMarshaller[comment]): Route =
    complete((200, responsecomment))
  /**
   * Code: 200, Message: add the given coomment to the specified property, DataType: comment
   */
  def propertiesPropertyIdPost(body: Comment, propertyId: String)
      (implicit toEntityMarshallercomment: ToEntityMarshaller[comment]): Route

}

trait DefaultApiMarshaller {
  implicit def fromRequestUnmarshallerComment: FromRequestUnmarshaller[Comment]

  implicit def fromRequestUnmarshallerSimple_property: FromRequestUnmarshaller[Simple_property]


  implicit def toEntityMarshallersimple_propertyarray: ToEntityMarshaller[List[simple_property]]

  implicit def toEntityMarshallersimple_property: ToEntityMarshaller[simple_property]

  implicit def toEntityMarshallercommented_property: ToEntityMarshaller[commented_property]

  implicit def toEntityMarshallercomment: ToEntityMarshaller[comment]

}

