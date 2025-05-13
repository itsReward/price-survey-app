package com.pricesurvey.dto.price

import java.math.BigDecimal

data class PriceEntryRequest(
    val storeId: Long,
    val productId: Long,
    val price: BigDecimal,
    val quantity: Int,
    val notes: String? = null
)

