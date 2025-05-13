package com.pricesurvey.dto.store

import java.math.BigDecimal

data class UpdateStoreRequest(
    val name: String?,
    val address: String?,
    val city: String?,
    val region: String?,
    val country: String?,
    val latitude: BigDecimal?,
    val longitude: BigDecimal?,
    val isActive: Boolean?
)