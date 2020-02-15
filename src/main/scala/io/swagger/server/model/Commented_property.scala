package io.swagger.server.model


/**
 * @param property 
 * @param comments 
 */
case class Commented_property ( property: Simple_property,
                                var comments: Option[List[Comment]]
                              )
{
  def add_comment(comment: Comment): Unit ={
    comments.getOrElse(Nil) ++: List(comment)
  }
}

