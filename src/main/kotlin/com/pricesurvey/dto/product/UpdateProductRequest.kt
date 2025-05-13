package com.pricesurvey.dto.product

data class UpdateProductRequest(
    val name: String?,
    val description: String?,
    val category: String?,
    val volumeMl: Int?,
    val brand: String?,
    val isActive: Boolean?
)