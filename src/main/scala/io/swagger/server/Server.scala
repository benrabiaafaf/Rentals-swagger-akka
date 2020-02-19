package io.swagger.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.marshalling.ToEntityMarshaller
import akka.http.scaladsl.server.Route
import io.swagger.server.Actors.DBManager
import io.swagger.server.api._
import io.swagger.server.model._
import spray.json.DefaultJsonProtocol
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.io.StdIn

object CostumContext {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  implicit val dBManager = system.actorOf(DBManager(), "DBManager")
  implicit val timeout = new Timeout(2 seconds)
}

object ApiService extends DefaultApiService {

  import CostumContext._

  /**
    * Code: 200, Message: a list of availble properties, DataType: List[simple_property]
    */

  //  type Route = RequestContext => Future[RouteResult]
  override def propertiesGet()(implicit toEntityMarshallersimple_propertyarray: ToEntityMarshaller[List[Simple_property]]): Route = {

    val response = (dBManager ? DBManager.get_all_properties()).mapTo[List[Simple_property]]
    requestcontext => {
      (response).flatMap {
        (properties: List[Simple_property]) =>
          propertiesGet200(properties)(toEntityMarshallersimple_propertyarray)(requestcontext)
      }
    }
  }

  /**
    * Code: 200, Message: add the given property, DataType: simple_property
    */
  override def propertiesPost(body: Simple_property)(implicit toEntityMarshallersimple_property: ToEntityMarshaller[Simple_property]): Route = {
    // TODO : body vérification
    val response = (dBManager ? DBManager.post_property(body)).mapTo[Simple_property]
    requestcontext => {
      (response).flatMap {
        (property: Simple_property) =>
          propertiesPost200(property)(toEntityMarshallersimple_property)(requestcontext)
      }
    }
  }

  /**
    * Code: 200, Message: get the specified property information with comments, DataType: commented_property
    */
  override def propertiesPropertyIdGet(propertyId: String)(implicit toEntityMarshallercommented_property: ToEntityMarshaller[Commented_property]): Route = {
    // TODO : deal with the None case
    val response = (dBManager ? DBManager.get_property(propertyId)).mapTo[Commented_property]
    requestcontext => {
      (response).flatMap {
        (property: Commented_property) =>
          if (property.property == None) {
            propertiesPropertyIdGet404(requestcontext)
          } else {
            propertiesPropertyIdGet200(property)(toEntityMarshallercommented_property)(requestcontext)
          }
      }
    }
  }

  /**
    * Code: 200, Message: add the given coomment to the specified property, DataType: comment
    */
  override def propertiesPropertyIdPost(body: Comment, propertyId: String)(implicit toEntityMarshallercomment: ToEntityMarshaller[Comment]): Route = {
    val response = (dBManager ? DBManager.post_comment_to_proprty(propertyId, body)).mapTo[Option[Comment]]
    requestcontext => {
      (response).flatMap {
        (comment: Option[Comment]) =>
          if (comment.getOrElse(None) != None) {
            propertiesPropertyIdPost200(comment.get)(toEntityMarshallercomment)(requestcontext)
          } else {
            propertiesPropertyIdPost404(requestcontext)
          }
      }
    }
  }
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

  import CostumContext._

  val api = new DefaultApi(ApiService, ApiMarshaller)
  val host = "localhost"
  val port = 8888
  val bindingFuture = Http().bindAndHandle(api.route, host, port)
  println(s"Server online at http://${host}:${port}/\nPress RETURN to stop...")

  bindingFuture.failed.foreach { ex =>
    println(s"${ex} Failed to bind to ${host}:${port}!")
  }

  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())

}
