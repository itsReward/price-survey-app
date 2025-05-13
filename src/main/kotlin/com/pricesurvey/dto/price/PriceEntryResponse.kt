package com.pricesurvey.dto.price

import java.math.BigDecimal

data class PriceEntryResponse(
    val id: Long,
    val user: UserInfo,
    val store: StoreInfo,
    val product: ProductInfo,
    val price: BigDecimal,
    val quantity: Int,
    val notes: String?,
    val createdAt: String,
    val updatedAt: String?
) {
    data class UserInfo(
        val id: Long,
        val email: String,
        val firstName: String,
        val lastName: String
    )

    data class StoreInfo(
        val id: Long,
        val name: String,
        val address: String,
        val city: String
    )

    data class ProductInfo(
        val id: Long,
        val name: String,
        val category: String,
        val volumeMl: Int,
        val brand: String?
    )
}
