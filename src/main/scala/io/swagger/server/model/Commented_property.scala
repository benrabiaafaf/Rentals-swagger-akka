package io.swagger.server.model


/**
 * @param property 
 * @param comments 
 */
case class Commented_property (
  property: simple_property,
  comments: Option[List[comment]]
)

