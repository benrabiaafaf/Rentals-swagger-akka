package io.swagger.server.model

import java.math.BigDecimal

/**
 * @param user_id 
 * @param year 
 * @param rent 
 */
case class Comment (
  user_id: String,
  year: Int,
  rent: BigDecimal
)

