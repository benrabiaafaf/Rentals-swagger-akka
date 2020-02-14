package io.swagger.server.model

import scala.math.BigDecimal

/**
 * @param property_id 
 * @param owner_id 
 * @param property_type
 * @param adress
 * @param rooms
 * @param area
 * @param rent
 * @param images
 */
case class Simple_property (
  property_id: Option[String],
  owner_id: Option[String],
  property_type: Option[String],
  adress: Option[String],
  rooms: Option[Int],
  area: Option[BigDecimal],
  rent: Option[BigDecimal],
  images: Option[List[String]]
)