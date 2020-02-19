# Description

The aim of this project is to create a real estate renting API using following technologies:

- OpenAPI3.
- Akka-http.
- Angular.

This API manages real estates for renting. owners add their properties to the API, and tenants can comment them (once a year) to provide theirs opinion and confirm the provided property description.

#  API description using OpenAPI 3. 
It consists of a yaml file describig / defining the API with general informations, availble paths and operations... 
[The OpenAPI map](http://openapi-map.apihandyman.io/?version=3.0) provides a very interessting shemas and a complete explanaision of the file structure.
The [official documentation](https://swagger.io/docs/specification/about/) can also be very usefull.
To do that, I used the [swaggerhub](https://app.swaggerhub.com/home).

- Operations and paths
    - Get all the availble properties on *~/properties*
    - Get a specified property by it's id (property informations listed above and it's comments) on   *~/properties/property_id*
    - Post a ew proprty on *~/properties*
    - Post a comment ( a single one by year and property) *~/properties/property_id*

- DataStructures
    - Simple property
    - Comment
    - Commented property  
    see [openapi](./openapi.yaml) and [models](./src/main/scala/io/swagger/server/model/) for details.

Once the API description finished, the scala-akka-http-server code is generated.

# Actors : The DBManager
I used a [json file](./src/main/scala/io/swagger/server/data/properties.json) to store the properties. 
It consists of a list of [Commented_property](./src/main/scala/io/swagger/server/model/Commented_property.scala).  
The DBManager manages this file by reading and writhing data.
It has four functions to ensure the four operations described above:
- get_all_properties : returns a list of [Simple_property](./src/main/scala/io/swagger/server/model/Simple_property.scala)
- get_property(property_id) :  returns an Option on [Commented_property](./src/main/scala/io/swagger/server/model/Commented_property.scala)
- post_property(simple_property) : returns an Option on [Simple_property](./src/main/scala/io/swagger/server/model/Simple_property.scala)
- post_comment_to_proprty(propertyId, comment) : return an Option on [Comment](./src/main/scala/io/swagger/server/model/Comment.scala)

Note that options here are used to deal with errors, for instance if the data can't be found or added.

# The Costum Json Protocol
In the [openapi](./openapi.yaml), I used components to create reusable objects.
The autogenerated code, did not support my datastructures, was getting an error from type ```could not find implicit value for evidence parameter of type...```  
In fact, my data structures contains Lists, Options and nested objects. 
In order to solve this, I had to create the CostumJsonProtocol and to import it where ever the models are used with Json.
```scala
object CostumJsonProtocol extends DefaultJsonProtocol {
  implicit val simple_propertyFormat = jsonFormat8(Simple_property)
  implicit val commentFormat = jsonFormat4(Comment)
  implicit val commeented_propertyFormat = jsonFormat2(Commented_property)
}
```

Note that the order of implicit val declaration is important, from the inner to the outter object.

# Application Server
consists of creating two objects extending ```DefaultApiService``` and ```DefaultApiMarshaller```  

[Marshalling](https://squbs.readthedocs.io/en/latest/marshalling/):  
Marshalling and unmarshalling is used both on the client and server side. On the server side it is used to map an incoming request to a Scala or Java object and to map a Scala or Java object to an outgoing response. Similarly, on the client side, it is used to marshal an object to an outgoing HTTP request and unmarshal it from an incoming response.    
Here, we will be using ```spray-json```

## ApiMarshaller
Make the ApiMarshaller use the CostumJsonProtocol
## ApiService
The service sends request to the DBManager, and treats the answers

# Perspectives
- Use Angular for frontend
- Create Other actors:
    - Client : 
        - takes client requests from the front end and sends them to the app manager
        - get back the answer, and show the answer
    - App Manager : 
        - takes requests from the client
        - makes necessary verification on the provided data
        - ask server to prosess the request
        - give back the answer to client
- Use MongoDB to manage data.
- Add users to data, date the comments.
- Authentification process.
- Provide more operations (PUT, DELETE...)

# What I learnd
- How to use openapi to provide a description for an api.
- How to manipulate akka-http to process requests.
- Learned more about version control on git.