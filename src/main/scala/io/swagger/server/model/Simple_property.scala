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
case class Simple_property (  property_id: String,
                              owner_id: String,
                              property_type: String,
                              adress: String,
                              rooms: Int,
                              area: BigDecimal,
                              rent: BigDecimal,
                              images: Option[List[String]]
                           )

