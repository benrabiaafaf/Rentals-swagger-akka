package io.swagger.server.model

import java.math.BigDecimal

/**
 * @param user_id 
 * @param property_id 
 * @param year 
 * @param rent 
 */
case class Comment (
  user_id: String,
  property_id: String,
  year: Int,
  rent: BigDecimal
)

