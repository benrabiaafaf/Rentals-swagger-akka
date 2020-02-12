package io.swagger.server.model

import java.math.BigDecimal

/**
 * @param property_id 
 * @param owner_id 
 * @param `type` 
 * @param adress 
 * @param rooms 
 * @param area 
 * @param rent 
 */
case class Simple_property (
  property_id: Option[String],
  owner_id: Option[String],
  `type`: Option[String],
  adress: Option[String],
  rooms: Option[Int],
  area: Option[BigDecimal],
  rent: Option[BigDecimal]
)

