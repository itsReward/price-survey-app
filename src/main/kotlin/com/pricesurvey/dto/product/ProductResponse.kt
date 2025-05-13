package com.pricesurvey.dto.product

data class ProductResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val category: String,
    val volumeMl: Int,
    val brand: String?,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String?
)