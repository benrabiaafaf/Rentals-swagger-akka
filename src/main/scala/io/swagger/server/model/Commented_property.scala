package io.swagger.server.model


/**
 * @param property 
 * @param comments 
 */
case class Commented_property (
  property: Simple_property,
  comments: Option[List[Comment]]
)

