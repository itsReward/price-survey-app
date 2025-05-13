package com.pricesurvey.dto.product

data class ProductRequest(
    val name: String,
    val description: String? = null,
    val category: String,
    val volumeMl: Int,
    val brand: String? = null
)
