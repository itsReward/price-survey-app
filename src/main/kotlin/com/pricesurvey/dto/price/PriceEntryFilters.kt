package com.pricesurvey.dto.price

import java.math.BigDecimal

data class PriceEntryFilters(
    val userId: Long? = null,
    val storeId: Long? = null,
    val productId: Long? = null,
    val startDate: String? = null,
    val endDate: String? = null
)