# To Start

The aim of this project is to create a real estate renting API using following technologies:

- OpenAPI3.
- Akka-http.
- Angular.

# Steps

1. Define what this API can be used for.
2. Create the API description using OpenAPI 3. 
    It consists of a yaml file describig / defining the API with general informations, availble paths and operations... 
    [The OpenAPI map](http://openapi-map.apihandyman.io/?version=3.0) provides a very interessting shemas and a complete explanaision of the file structure.
    The [official documentation](https://swagger.io/docs/specification/about/) is also very usefull.
3. Code generation scala-akka-http-server.

For steps 2 and 3, I used the [swaggerhub](https://app.swaggerhub.com/home) online.

4. Write the logic behid the server
    - Create two objects extending ```DefaultApiService``` and ```DefaultApiMarshaller```
    - [Marshalling](https://squbs.readthedocs.io/en/latest/marshalling/):  
    Marshalling and unmarshalling is used both on the client and server side. On the server side it is used to map an incoming request to a Scala or Java object and to map a Scala or Java object to an outgoing response. Similarly, on the client side, it is used to marshal an object to an outgoing HTTP request and unmarshal it from an incoming response.    
    Here, we will be using ```spray-json```