package com.pricesurvey.dto.store

import java.math.BigDecimal


data class StoreResponse(
    val id: Long,
    val name: String,
    val address: String,
    val city: String,
    val region: String,
    val country: String,
    val latitude: BigDecimal?,
    val longitude: BigDecimal?,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String?
)
